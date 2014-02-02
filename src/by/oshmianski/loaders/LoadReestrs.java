package by.oshmianski.loaders;

import by.oshmianski.objects.DataMainItem;
import by.oshmianski.objects.Importer;
import by.oshmianski.objects.Reestr;
import by.oshmianski.ui.edt.UIProcessor;
import by.oshmianski.utils.AppletParams;
import by.oshmianski.utils.MyLog;
import lotus.domino.*;

public class LoadReestrs implements Runnable, Loader {
    private boolean executed = false;
    private boolean canceled = false;

    /**
     * UI callback
     */
    private UIProcessor ui;
    private Importer importer;

    private String moduleName;

    public LoadReestrs(UIProcessor ui) {
        super();

        this.ui = ui;

        moduleName = "Загрузка списка реестров";
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
//        ui.startLoading();
        System.out.println("Load reestr...");
        ui.setButtonLoadReestrsEnable(false);

        try {
            work();
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            //окончание
            executed = false;
//            ui.stopLoading();
            ui.setButtonLoadReestrsEnable(true);
            System.out.println("Load reestr...Ok");
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
            viewSuit = db.getView(AppletParams.getInstance().getViewSuit());

            viewSuit.setAutoUpdate(false);

            Reestr reestr;

            i = 0;
            ui.clearReestrItems();

            nav = viewSuit.createViewNav();

            if (Integer.valueOf(session.evaluate("@Version").firstElement().toString()) >= 379) {
                nav.setBufferMaxEntries(400);
                nav.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
            }

            ve = nav.getFirst();
            while (ve != null) {
                if (!ve.isTotal()) {
                    reestr = new Reestr(ve.getColumnValues().get(1).toString());

                    ui.appendReestr(reestr);
                }

                vetmp = nav.getNextSibling();
                ve.recycle();
                ve = vetmp;

                reestr = null;

                if (isCanceled()) break;
                i++;
            }

            ui.setFilteredCount();
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

    public UIProcessor getUi() {
        return ui;
    }

    public boolean isExecuted() {
        return executed;
    }
}
