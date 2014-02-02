package by.oshmianski.objects;

import by.oshmianski.loaders.LoadMainData;
import by.oshmianski.ui.edt.UIProcessor;
import by.oshmianski.utils.AppletParams;
import by.oshmianski.utils.MyLog;
import lotus.domino.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: VintsalovichS
 * Date: 02.10.13
 * Time: 10:40
 */
public class Importer {
    private LoadMainData loader;
    private UIProcessor ui;

    private final SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private DecimalFormat formatNumber = new DecimalFormat("0.######");

    public Importer(LoadMainData loader) {
        this.loader = loader;
        this.ui = loader.getUi();
        formatNumber.setGroupingUsed(false);
    }

    public void process() {
        test();
    }

    private void test() {
        ui.setProgressLabelText("Чтение данных...");
        startTest();
        ui.setProgressLabelText("Чтение данных...OK");
    }

    private void startTest() {
        Session session = null;
        Database db = null;
        View viewSuit = null;
        View viewDebt = null;
        View viewDebtExt = null;
        View viewMove = null;
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
            viewDebt = db.getView(AppletParams.getInstance().getViewDebt());
            viewDebtExt = db.getView(AppletParams.getInstance().getViewDebtExt());
            viewMove = db.getView(AppletParams.getInstance().getViewMove());

            viewSuit.setAutoUpdate(false);
            viewDebt.setAutoUpdate(false);
            viewDebtExt.setAutoUpdate(false);
            viewMove.setAutoUpdate(false);

            DataMainItem dataMainItem;

            ui.clearDataMainItems();
            ui.setDataDockReestr(loader.getReestr());

            i = 0;

            nav = viewSuit.createViewNavFromCategory(loader.getReestr());

            if (Integer.valueOf(session.evaluate("@Version").firstElement().toString()) >= 379) {
                nav.setBufferMaxEntries(400);
                nav.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
            }

            count = nav.getCount();

            ui.setFilteredCount();
            ui.setProgressValue(0);
            ui.setProgressMaximum(count);

            ve = nav.getFirst();
            while (ve != null) {
                dataMainItem = processItem(session, db, ve, viewDebt, viewDebtExt, viewMove);

                ui.appendDataMainItem(dataMainItem);

                vetmp = nav.getNext();
                ve.recycle();
                ve = vetmp;

                dataMainItem = null;

                ui.setProgressValue(i + 1);

                if (loader.isCanceled()) break;
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
                if (viewDebt != null) {
                    viewDebt.recycle();
                }
                if (viewDebtExt != null) {
                    viewDebtExt.recycle();
                }
                if (viewMove != null) {
                    viewMove.recycle();
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

    private DataMainItem processItem(
            Session session,
            Database db,
            ViewEntry ve,
            View viewDebt,
            View viewDebtExt,
            View viewMove
    ) {

        DataMainItem dataMainItem = null;

        DocumentCollection colDebt = null;
        DocumentCollection colDebtExt = null;
        DocumentCollection colMove = null;

        Document noteSuit = null;
        Document noteDebt = null;
        Document noteDebtExt = null;
        Document noteMove = null;
        Document noteTmp = null;

        DateTime dtDebtExt = null;

        Item item = null;

        BigDecimal sumSalary = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumSalaryAll = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumDebt = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumMove = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumDebtAll = new BigDecimal(BigInteger.ZERO);
        BigDecimal debtFeeRate = new BigDecimal(BigInteger.ZERO);

        StringBuilder accounts = new StringBuilder();
        String separator = "";
        int sumType = ui.getSumType();

        boolean isExclude = true;

        try {
            noteSuit = ve.getDocument();

            colDebt = viewDebt.getAllDocumentsByKey(noteSuit.getUniversalID(), true);
            noteDebt = colDebt.getFirstDocument();
            while (noteDebt != null) {
                if (!"1".equalsIgnoreCase(noteDebt.getItemValueString("FLAG_NOTEXPORT"))) {
                    switch (sumType) {
                        case 3:
                            colDebtExt = viewDebtExt.getAllDocumentsByKey(noteDebt.getUniversalID(), true);
                            if (colDebtExt.getCount() == 0) {
                                sumMove = getSumMove(noteDebt, viewMove);
                                sumDebt = getSumDebtFirst(noteDebt);

                                sumDebt = sumDebt.subtract(sumMove);
                                debtFeeRate = new BigDecimal(noteDebt.getItemValueDouble("debtFeeRate"));
                            } else {
                                noteDebtExt = getLastDebtExtByDate(db, colDebtExt);

                                if(item != null){
                                    item.recycle();
                                }
                                item = noteDebtExt.getFirstItem("importFileReestDate");
                                dtDebtExt = item.getDateTimeValue();
                                sumMove = getSumMoveAfterDate(noteDebt, viewMove, dtDebtExt);
                                sumDebt = new BigDecimal(noteDebtExt.getItemValueDouble("debtSum"));

                                sumDebt = sumDebt.subtract(sumMove);
                                debtFeeRate = new BigDecimal(noteDebtExt.getItemValueDouble("debtFeeRate"));
                            }

                            break;

                        case 2:
                            sumMove = getSumMove(noteDebt, viewMove);
                            sumDebt = getSumDebtFirst(noteDebt);

                            sumDebt = sumDebt.subtract(sumMove);
                            debtFeeRate = new BigDecimal(noteDebt.getItemValueDouble("debtFeeRate"));

                            break;

                        case 1:
                            sumDebt = getSumDebtFirst(noteDebt);
                            debtFeeRate = new BigDecimal(noteDebt.getItemValueDouble("debtFeeRate"));

                            break;

                        default:
                    }

                    sumDebtAll = sumDebtAll.add(sumDebt);
                    sumSalaryAll = sumSalaryAll.add(sumDebt.multiply(debtFeeRate));

                    accounts.append(separator);
                    accounts.append(noteDebt.getItemValueString("debtPersonalAccount"));
                    separator = ",";

                    isExclude = false;
                }

                noteTmp = colDebt.getNextDocument();
                noteDebt.recycle();
                noteDebt = noteTmp;

                if (colDebtExt != null) {
                    colDebtExt.recycle();
                }
            }

            dataMainItem = new DataMainItem(
                    ve.getUniversalID(),
                    accounts.toString(),
                    noteSuit.getItemValueString("fio"),
                    sumDebtAll,
                    sumSalaryAll,
                    "1".equalsIgnoreCase(noteSuit.getItemValueString("isDraft")),
                    isExclude);

        } catch (
                Exception e
                )

        {
            MyLog.add2Log(e);
        } finally

        {
            try {
                if(item != null){
                    item.recycle();
                }
                if (dtDebtExt != null) {
                    dtDebtExt.recycle();
                }
                if (noteSuit != null) {
                    noteSuit.recycle();
                }
                if (noteTmp != null) {
                    noteTmp.recycle();
                }
                if (noteDebt != null) {
                    noteDebt.recycle();
                }
                if (noteDebtExt != null) {
                    noteDebtExt.recycle();
                }
                if (noteMove != null) {
                    noteMove.recycle();
                }
                if (colDebtExt != null) {
                    colDebtExt.recycle();
                }
                if (colDebt != null) {
                    colDebt.recycle();
                }
                if (colMove != null) {
                    colMove.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return dataMainItem;
    }

    private BigDecimal getSumMove(Document noteDebt, View viewMove) {
        BigDecimal sum = new BigDecimal(BigInteger.ZERO);

        DocumentCollection colMove = null;
        Document noteMove = null;
        Document noteMoveTmp = null;

        try {
            colMove = viewMove.getAllDocumentsByKey(noteDebt.getUniversalID(), true);

            noteMove = colMove.getFirstDocument();
            while (noteMove != null) {
                sum = sum.add(new BigDecimal(noteMove.getItemValueDouble("summ")));

                noteMoveTmp = colMove.getNextDocument();
                noteMove.recycle();
                noteMove = noteMoveTmp;
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if (noteMoveTmp != null) {
                    noteMoveTmp.recycle();
                }
                if (noteMove != null) {
                    noteMove.recycle();
                }
                if (colMove != null) {
                    colMove.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return sum;
    }

    private BigDecimal getSumDebtFirst(Document noteDebt) {
        BigDecimal sum = new BigDecimal(BigInteger.ZERO);

        Double debtSumPercent;
        Double debtSumPenalty;
        Double debtSum;
        Double debtSumFine;

        try {
            String debtType = noteDebt.getItemValueString("debtType");
            Integer detTypeInt = Integer.valueOf(debtType);

            switch (detTypeInt) {
                case 21:
                    debtSumPenalty = noteDebt.getItemValueDouble("debtSumPenalty");
                    debtSum = noteDebt.getItemValueDouble("debtSum");
                    sum = new BigDecimal(debtSumPenalty).add(new BigDecimal(debtSum));

                    break;
                case 22:
                    debtSumPercent = noteDebt.getItemValueDouble("debtSumPercent");
                    debtSumFine = noteDebt.getItemValueDouble("debtSumFine");
                    debtSumPenalty = noteDebt.getItemValueDouble("debtSumPenalty");
                    debtSum = noteDebt.getItemValueDouble("debtSum");
                    sum = new BigDecimal(debtSumFine).add(new BigDecimal(debtSumPenalty).add(new BigDecimal(debtSum)).add(new BigDecimal(debtSumPercent)));

                    break;
                case 23:
                    sum = new BigDecimal(noteDebt.getItemValueDouble("debtSum"));

                    break;
                default:

            }
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
        }

        return sum;
    }

    private Document getLastDebtExtByDate(Database db, DocumentCollection colDebtExt) {
        Document noteDebtExtReturn = null;
        Document noteDebtExt = null;
        Document noteDebtExtTmp = null;

        Item item = null;

        DateTime dtDebtExt = null;
        DateTime dtDebtExtTmp = null;

        String unid = "";

        try {
            noteDebtExt = colDebtExt.getFirstDocument();
            item = noteDebtExt.getFirstItem("importFileReestDate");
            dtDebtExt = item.getDateTimeValue();
            unid = noteDebtExt.getUniversalID();

            while (noteDebtExt != null) {
                item.recycle();
                item = noteDebtExt.getFirstItem("importFileReestDate");
                dtDebtExtTmp = item.getDateTimeValue();

                if(dtDebtExtTmp.timeDifference(dtDebtExt) > 0){
                    unid = noteDebtExt.getUniversalID();
                    dtDebtExt.recycle();
                    dtDebtExt = item.getDateTimeValue();
                }

                noteDebtExtTmp = colDebtExt.getNextDocument();
                noteDebtExt.recycle();
                noteDebtExt = noteDebtExtTmp;

                if(dtDebtExtTmp != null){
                    dtDebtExtTmp.recycle();
                }
            }

            noteDebtExtReturn = db.getDocumentByUNID(unid);

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if(item != null){
                    item.recycle();
                }
                if(dtDebtExtTmp != null){
                    dtDebtExtTmp.recycle();
                }
                if (dtDebtExt != null) {
                    dtDebtExt.recycle();
                }
                if (noteDebtExt != null) {
                    noteDebtExt.recycle();
                }
                if (noteDebtExtTmp != null) {
                    noteDebtExtTmp.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return noteDebtExtReturn;
    }

    private BigDecimal getSumMoveAfterDate(Document noteDebt, View viewMove, DateTime dtDebtExt) {
        BigDecimal sum = new BigDecimal(BigInteger.ZERO);

        DateTime dtMove = null;

        DocumentCollection colMove = null;
        Document noteMove = null;
        Document noteMoveTmp = null;

        Item item = null;

        try {
            colMove = viewMove.getAllDocumentsByKey(noteDebt.getUniversalID(), true);

            noteMove = colMove.getFirstDocument();
            while (noteMove != null) {
                item = noteMove.getFirstItem("dateOperation");
                dtMove = item.getDateTimeValue();

                if (dtDebtExt.timeDifference(dtMove) < 0) {
                    sum = sum.add(new BigDecimal(noteMove.getItemValueDouble("summ")));
                }

                noteMoveTmp = colMove.getNextDocument();
                noteMove.recycle();
                noteMove = noteMoveTmp;

                if(item != null){
                    item.recycle();
                }
            }

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            try {
                if(item != null){
                    item.recycle();
                }
                if (dtMove != null) {
                    dtMove.recycle();
                }
                if (noteMoveTmp != null) {
                    noteMoveTmp.recycle();
                }
                if (noteMove != null) {
                    noteMove.recycle();
                }
                if (colMove != null) {
                    colMove.recycle();
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return sum;
    }
}
