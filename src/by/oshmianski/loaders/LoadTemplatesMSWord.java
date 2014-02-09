package by.oshmianski.loaders;

import by.oshmianski.objects.TemplateWord;
import by.oshmianski.utils.AppletParams;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import lotus.domino.*;

public class LoadTemplatesMSWord implements Runnable, Loader {
    private boolean executed = false;
    private boolean canceled = false;

    private String moduleName;
    private EventList<TemplateWord> sink;

    public LoadTemplatesMSWord(EventList<TemplateWord> items) {
        super();

        this.sink = GlazedLists.threadSafeList(items);

        moduleName = "Загрузка списка шаблонов MS Word";
    }

    /**
     * Starts this loader execution in separate thread
     */
    @Override
    public synchronized void execute() {
        if (executed) {
//            throw new IllegalStateException("Loader is already executed");
            return;
        }
        executed = true;
        canceled = false;
        Thread t = new Thread(this, "Custom loader thread " + moduleName);
        t.start();
    }

    /**
     * Loader main cycle
     */
    @Override
    public void run() {
        //действия вначале
        System.out.println("Load templatesWord...");

        try {
            work();
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            //окончание
            executed = false;
            System.out.println("Load templatesWord...Ok");
        }
    }

    /**
     * Cancels current loading
     */
    @Override
    public synchronized void cancel() {
        canceled = true;
    }

    public synchronized boolean isCanceled() {
        return canceled;
    }

    protected void work() {
        Session session = null;
        Database db = null;
        View viewSuit = null;
        ViewNavigator nav = null;
        ViewEntry ve = null;
        ViewEntry vetmp = null;

        int count = 0;
        int i = 0;

        try {
            NotesThread.sinitThread();
            session = NotesFactory.createSession();

            db = session.getDatabase(null, null);
            db.openByReplicaID(AppletParams.getInstance().getServer(), AppletParams.getInstance().getDbIDProc());
            viewSuit = db.getView(AppletParams.getInstance().getViewTemplateWord());

            viewSuit.setAutoUpdate(false);

            i = 0;
            sink.clear();

            nav = viewSuit.createViewNav();

            if (Integer.valueOf(session.evaluate("@Version").firstElement().toString()) >= 379) {
                nav.setBufferMaxEntries(400);
                nav.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
            }

            TemplateWord templateWord;

            ve = nav.getFirst();
            while (ve != null) {
                if (!ve.isTotal() && !ve.isCategory()) {
                    templateWord = new TemplateWord(
                            ve.getUniversalID(),
                            ve.getColumnValues().get(3).toString(),
                            ve.getColumnValues().get(4).toString()
                    );

                    sink.add(templateWord);
                }

                vetmp = nav.getNext();
                ve.recycle();
                ve = vetmp;

//                templateWord = null;

                if (isCanceled()) break;
                i++;
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (vetmp != null) {
                    vetmp.recycle();
                }
                if (ve != null) {
                    ve.recycle();
                }
                if (nav != null) {
                    nav.recycle();
                }
                if (viewSuit != null) {
                    viewSuit.recycle();
                }
                if (db != null) {
                    db.recycle();
                }
                if (session != null) {
                    session.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }

            try {
                NotesThread.stermThread();
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }
    }

    public boolean isExecuted() {
        return executed;
    }
}
