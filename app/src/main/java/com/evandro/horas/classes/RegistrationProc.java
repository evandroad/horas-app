package com.evandro.horas.classes;

import android.os.Handler;
import android.os.Message;

import com.evandro.horas.screens.RegistrationActivity;
import com.evandro.horas.util.FileUtil;

import java.util.Collections;

public class RegistrationProc extends Thread{

    public static final int PROC_STATE_IDLE = 0;
    public static final int PROC_STATE_SAVE = 1;

    private final Handler handler;
    private boolean threadLoop;
    private int procState;
    private String date, entry, intEntry, intExit, exit;

    public RegistrationProc(Handler handler) {
        this.handler = handler;
        this.threadLoop = true;
        this.procState = PROC_STATE_IDLE;
    }

    public void finalize() { this.threadLoop = false; }

    public void setProcState(int procState) { this.procState = procState; }

    public void setDate(String date) { this.date = date; }

    public void setEntry(String entry) { this.entry = entry; }

    public void setIntEntry(String intEntry) { this.intEntry = intEntry; }

    public void setIntExit(String intExit) { this.intExit = intExit; }

    public void setExit(String exit) { this.exit = exit; }

    private void save() {
        if (checkInputs() == 1) {
            updateUI(RegistrationActivity.MSG_FORMAT_INCORRECT, null);
            finalize();
            return;
        }

        Register r = new Register(date, entry, intEntry, intExit, exit);

        Records records = Records.getInstance();
        records.setRecords(r);

        if (entry.length() > 1 && intEntry.length() > 1 && intExit.length() > 1 && exit.length() > 1) {
            updateUI(RegistrationActivity.MSG_RENDER, null);
        }

        records.getRecords().removeIf(re -> re.getDate().equals(r.getDate()));
        records.setRecords(r);
        Collections.sort(records.getRecords(), Collections.reverseOrder());
        FileUtil.saveStringToFile(Records.getFile(), Records.getInstance().toString());

        updateUI(RegistrationActivity.MSG_SUCCESS, null);
        finalize();
    }

    private int checkInputs() {
        int r = 0;
        String[] inputs = {entry, intEntry, intExit, exit};
//        String[] arr1 = entry.split(":");
//        String[] arr2 = intEntry.split(":");
//        String[] arr3 = intExit.split(":");
//        String[] arr4 = exit.split(":");

        if (lengthHour(entry.length()) || lengthHour(intEntry.length()) || lengthHour(intExit.length()) ||
                lengthHour(exit.length())) {
            r = 1;
        } else {
            for (int i = 0; i < 4; i++) {
                String[] arr = inputs[i].split(":");
                if (inputs[i].length() == 5) {
                    if (rangeHour(Integer.parseInt(arr[0]))) {
                        if (rangeMin(Integer.parseInt(arr[1]))) { r = 0; }
                        else { r = 1; }
                    } else {
                        r = 1;
                    }
                }
            }
//            if (intEntry.length() == 5) {
//                if (rangeHour(Integer.parseInt(arr2[0]))) {
//                    if (rangeMin(Integer.parseInt(arr2[1]))) { r = 0; }
//                    else { r = 1; }
//                } else {
//                    r = 1;
//                }
//            }
//            if (intExit.length() == 5) {
//                if (rangeHour(Integer.parseInt(arr3[0]))) {
//                    if (rangeMin(Integer.parseInt(arr3[1]))) { r = 0; }
//                    else { r = 1; }
//                } else {
//                    r = 1;
//                }
//            }
//            if (exit.length() == 5) {
//                if (rangeHour(Integer.parseInt(arr4[0]))) {
//                    if (rangeMin(Integer.parseInt(arr4[1]))) { r = 0; }
//                    else { r = 1; }
//                } else {
//                    r = 1;
//                }
//            }
        }

        return r;
    }

    private static boolean lengthHour(int n) { return Math.max(1, n) == Math.min(n, 4); }

    private static boolean rangeHour(int n) {
        boolean r = Math.max(0, n) == Math.min(n, 23);
        return r;
    }

    private static boolean rangeMin(int n) {
        boolean r = Math.max(0, n) == Math.min(n, 59);
        return r;
    }

    public void run() {
        while (threadLoop) {
            try {
                int _state = this.procState;
                this.procState = PROC_STATE_IDLE;

                switch (_state) {
                    case PROC_STATE_SAVE:
                        this.save();
                        break;
                    default:
                        break;
                }

                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void updateUI(int code, Object msg) {
        Message _message = new Message();
        _message.what = code;
        _message.obj = msg;
        handler.sendMessage(_message);
    }

}
