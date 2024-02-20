package com.example.rfidlibrarymanagementsystem;

import com.clou.uhf.G3Lib.CLReader;
import com.clou.uhf.G3Lib.Enumeration.eReadType;
import com.clou.uhf.G3Lib.ClouInterface.IAsynchronousMessage;
import com.clou.uhf.G3Lib.ClouInterface.ISearchDevice;
import com.clou.uhf.G3Lib.Protocol.Device_Mode;
import com.clou.uhf.G3Lib.Protocol.Tag_Model;

public class Main1 implements IAsynchronousMessage,ISearchDevice {

    private String currentEPC;

    public String getCurrentEPC() {
        return currentEPC;
    }

    public static void main(String[] args) {
        Main1 example = new Main1();
        String ConnID = "192.168.1.116:9090";
        eReadType Single = eReadType.Single;
        eReadType Inventory = eReadType.Inventory;
        Integer Ant = 1;


        try {
            if (CLReader.CreateTcpConn("192.168.1.116:9090", example)) {
                System.out.println("Connect success!\n");
            } else {
                System.out.println("Connect failure!\n");
            }
            System.out.println("Current ConnID : " + ConnID + "\n");

            int a = CLReader._Tag6C.GetEPC(ConnID, Ant, Single);

            if (a == 0) {
                System.out.println("Success!" + a);

                // Start a separate thread for the output loop
                new Thread(() -> {
                    showEPC();
                }).start();
            } else {
                System.out.println("Failure!" + a);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void showEPC() {
        for (int i = 0; i <= 100; i++) { // Adjust the loop limit
//            System.out.println("Output: " + i);
            try {
                // Adjust the sleep duration based on your desired output speed
                Thread.sleep(100); // Adjust the sleep duration
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void DebugMsg(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void DeviceInfo(Device_Mode arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void GPIControlMsg(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OutPutTags(Tag_Model arg0) {
        // TODO Auto-generated method stub
        System.out.println("EPC��"+ arg0._EPC + " TID��" + arg0._TID + " ReaderName��" + arg0._ReaderName);
        currentEPC = arg0._EPC; // Update the currentEPC field
    }

    @Override
    public void OutPutTagsOver() {
        // TODO Auto-generated method stub

    }

    @Override
    public void PortClosing(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void PortConnecting(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void WriteDebugMsg(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void WriteLog(String arg0) {
        // TODO Auto-generated method stub

    }


}