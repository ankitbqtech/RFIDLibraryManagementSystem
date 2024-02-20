package com.example.rfidlibrarymanagementsystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TimeZone;

import com.clou.uhf.G3Lib.CLReader;
import com.clou.uhf.G3Lib.Param_Set;
import com.clou.uhf.G3Lib.RFID_Option;
import com.clou.uhf.G3Lib.UHFConifg;
import com.clou.uhf.G3Lib.Enumeration.eAntennaNo;
import com.clou.uhf.G3Lib.Enumeration.eBaudrate;
import com.clou.uhf.G3Lib.Enumeration.eGPI;
import com.clou.uhf.G3Lib.Enumeration.eGPO;
import com.clou.uhf.G3Lib.Enumeration.eGPOState;
import com.clou.uhf.G3Lib.Enumeration.eLockArea;
import com.clou.uhf.G3Lib.Enumeration.eLockType;
import com.clou.uhf.G3Lib.Enumeration.eRF_Range;
import com.clou.uhf.G3Lib.Enumeration.eReadType;
import com.clou.uhf.G3Lib.Enumeration.eTriggerCode;
import com.clou.uhf.G3Lib.Enumeration.eTriggerStart;
import com.clou.uhf.G3Lib.Enumeration.eTriggerStop;
import com.clou.uhf.G3Lib.Enumeration.eWiegandDetails;
import com.clou.uhf.G3Lib.Enumeration.eWiegandFormat;
import com.clou.uhf.G3Lib.Enumeration.eWiegandSwitch;
import com.clou.uhf.G3Lib.Enumeration.eWorkMode;
import com.clou.uhf.G3Lib.ClouInterface.IAsynchronousMessage;
import com.clou.uhf.G3Lib.ClouInterface.ISearchDevice;
import com.clou.uhf.G3Lib.Helper.Helper_String;
import com.clou.uhf.G3Lib.Protocol.Device_Mode;
import com.clou.uhf.G3Lib.Protocol.Tag_Model;
import com.fasterxml.jackson.core.JsonParseException;
import com.jamierf.rxtx.RXTXLoader;

public class Main2 implements IAsynchronousMessage,ISearchDevice {

    public static void main(String[] args) {
        try {
            RXTXLoader.load();        //add library about rxtx
        } catch (Exception ex) {
        }

        Scanner sc = new Scanner(System.in);

        String readKey;
        Main2 example = new Main2();
        String ConnID = "";

        while (true) {
            System.out.println("Please select connect type: 1.RS232 2.TCP 3.RS485 q.Quit\n");
            readKey = sc.next();
            if (readKey.equals("1")) {
                System.out.println("Please input RS232 ConnID,Format: 'COM Number':'Baud Rate' like \"COM1:115200\" \n");
                ConnID = sc.next();
                if (CLReader.CreateSerialConn(ConnID, example)) {
                    System.out.println("Connect success!\n");
                    break;
                } else {
                    System.out.println("Connect failure!\n");
                    continue;
                }
            } else if (readKey.equals("2")) {
                System.out.println("Please input TCP ConnID,Format: 'IP Address':'Connect Port' like \"192.168.1.116:9090\" \n");
                ConnID = sc.next();
                if (CLReader.CreateTcpConn(ConnID, example)) {
                    System.out.println("Connect success!\n");
                    break;
                } else {
                    System.out.println("Connect failure!\n");
                    continue;
                }
            } else if (readKey.equals("3")) {
                System.out.println("Please input RS485 ConnID,Format: '485 Address':'COM Number':'Baud Rate' like \"1:COM1:115200\" \n");
                ConnID = sc.next();
                if (CLReader.Create485Conn(ConnID, example)) {
                    System.out.println("Connect success!\n");
                    break;
                } else {
                    System.out.println("Connect failure!\n");
                    continue;
                }
            } else if (readKey.equals("q")) {
                CLReader.Stop(ConnID);
                CLReader.CloseConn(ConnID);
                return;
            } else {
                System.out.println("Select Parameter Error!\n");
            }
        }


        try {
            while (true) {
                System.out.println("Current ConnID : " + ConnID + "\n");
                System.out.println("Please select operation:");
                System.out.println("1.Connect Setting");
                System.out.println("2.Reader Setting");
                System.out.println("3.RFID Setting");
                System.out.println("4.GPIO Setting");
                System.out.println("5.6C Tag");
                System.out.println("6.6B Tag");
                System.out.println("7.GB Tag");
                System.out.println("8.Break Point");
                System.out.println("q.Quit");

                readKey = sc.next();

                if (readKey.equals("1")) {
                    while (true) {
                        System.out.println("Current ConnID : " + ConnID + "\n");
                        System.out.println("Please select operation:");
                        System.out.println("1.Close Single Connect");
                        System.out.println("2.Close All Connect");
                        System.out.println("b.Back to Previous Menu");
                        System.out.println("q.Quit");

                        readKey = sc.next();
                        if (readKey.equals("1")) {
                            System.out.println("Please input ConnID");
                            readKey = sc.next();
                            CLReader.CloseConn(readKey);
                            if (readKey.equals(ConnID)) {
                                return;
                            }
                        } else if (readKey.equals("2")) {
                            CLReader.CloseAllConnect();
                            return;
                        } else if (readKey.equals("b")) {
                            break;
                        } else if (readKey.equals("q")) {
                            CLReader.Stop(ConnID);
                            CLReader.CloseConn(ConnID);
                            return;
                        }
                    }
                } else if (readKey.equals("5")) {
                    while (true) {
                        System.out.println("Current ConnID : " + ConnID + "\n");
                        System.out.println("Please select operation:");
                        System.out.println("1.Read 6C Tag");
                        System.out.println("2.Write 6C Tag");
                        System.out.println("3.Lock 6C Tag");
                        System.out.println("4.Kill 6C Tag");
                        System.out.println("b.Back to Previous Menu");
                        System.out.println("s.Stop Read");
                        System.out.println("q.Quit");

                        readKey = sc.next();


                        if (readKey.equals("1")) {
                            while (true) {
                                System.out.println("Please select method:");
                                System.out.println("1.GetEPC(String ConnID, int antNum, eReadType readType)");
                                System.out.println("2.GetEPC(String ConnID, int antNum, eReadType readType, String accessPassword)");
                                System.out.println("3.GetEPC_MatchEPC(String ConnID, int antNum, eReadType readType, String sEPC)");
                                System.out.println("4.GetEPC_MatchEPC(String ConnID, int antNum, eReadType readType, String sEPC, int matchWordStartIndex)");
                                System.out.println("5.GetEPC_MatchEPC(String ConnID, int antNum, eReadType readType, String sEPC, int matchWordStartIndex, String accessPassword)");
                                System.out.println("6.GetEPC_MatchTID(String ConnID, int antNum, eReadType readType, String sTID)");
                                System.out.println("7.GetEPC_MatchTID(String ConnID, int antNum, eReadType readType, String sTID, int matchWordStartIndex)");
                                System.out.println("8.GetEPC_MatchTID(String ConnID, int antNum, eReadType readType, String sTID, int matchWordStartIndex, String accessPassword)");
                                System.out.println("9.GetEPC_TID(String ConnID, int antNum, eReadType readType)");
                                System.out.println("10.GetEPC_TID(String ConnID, int antNum, eReadType readType, String accessPassword)");
                                System.out.println("11.GetEPC_TID_MatchEPC(String ConnID, int antNum, eReadType readType, String sEPC)");
                                System.out.println("12.GetEPC_TID_MatchEPC(String ConnID, int antNum, eReadType readType, String sEPC, int matchWordStartIndex)");
                                System.out.println("13.GetEPC_TID_MatchEPC(String ConnID, int antNum, eReadType readType, String sEPC, int matchWordStartIndex, String accessPassword)");
                                System.out.println("14.GetEPC_TID_MatchTID(String ConnID, int antNum, eReadType readType, String sTID)");
                                System.out.println("15.GetEPC_TID_MatchTID(String ConnID, int antNum, eReadType readType, String sTID, int matchWordStartIndex)");
                                System.out.println("16.GetEPC_TID_MatchTID(String ConnID, int antNum, eReadType readType, String sTID, int matchWordStartIndex, String accessPassword)");
                                System.out.println("17.GetEPC_TID_UserData(String ConnID, int antNum, eReadType readType,int readStart, int readLen)");
                                System.out.println("18.GetEPC_TID_UserData(String ConnID, int antNum, eReadType readType,int readStart, int readLen, String accessPassword)");
                                System.out.println("19.GetEPC_TID_UserData_MatchEPC(String ConnID, int antNum, eReadType readType,int readStart, int readLen, String sEPC)");
                                System.out.println("20.GetEPC_TID_UserData_MatchEPC(String ConnID, int antNum, eReadType readType,int readStart, int readLen, String sEPC, int matchWordStartIndex)");
                                System.out.println("21.GetEPC_TID_UserData_MatchEPC(String ConnID, int antNum, eReadType readType,int readStart, int readLen, String sEPC, int matchWordStartIndex, String accessPassword)");
                                System.out.println("22.GetEPC_TID_UserData_MatchTID(String ConnID, int antNum, eReadType readType,int readStart, int readLen, String sTID)");
                                System.out.println("23.GetEPC_TID_UserData_MatchTID(String ConnID, int antNum, eReadType readType,int readStart, int readLen, String sTID, int matchWordStartIndex)");
                                System.out.println("24.GetEPC_TID_UserData_MatchTID(String ConnID, int antNum, eReadType readType,int readStart, int readLen, String sTID, int matchWordStartIndex, String accessPassword)");
                                System.out.println("s.Stop Read");
                                System.out.println("b.Back to Previous Menu");
                                System.out.println("q.Quit");

                                readKey = sc.next();

                                if (readKey.equals("1")) {
                                    int antNum = 0;
                                    while (true) {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1")) {
                                            antNum += eAntennaNo._1.GetNum();
                                        } else if (readKey.equals("2")) {
                                            antNum += eAntennaNo._2.GetNum();
                                        } else if (readKey.equals("3")) {
                                            antNum += eAntennaNo._3.GetNum();
                                        } else if (readKey.equals("4")) {
                                            antNum += eAntennaNo._4.GetNum();
                                        } else if (readKey.equals("5")) {
                                            antNum += eAntennaNo._5.GetNum();
                                        } else if (readKey.equals("6")) {
                                            antNum += eAntennaNo._6.GetNum();
                                        } else if (readKey.equals("7")) {
                                            antNum += eAntennaNo._7.GetNum();
                                        } else if (readKey.equals("8")) {
                                            antNum += eAntennaNo._8.GetNum();
                                        } else {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1")) {
                                            continue;
                                        } else if (readKey.equals("2")) {
                                            break;
                                        } else {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1")) {
                                        readType = eReadType.Single;
                                    } else if (readKey.equals("2")) {
                                        readType = eReadType.Inventory;
                                    } else {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    int a = CLReader._Tag6C.GetEPC("192.168.1.116:9090", 1, eReadType.Single);

                                    if (a == 0) {
                                        System.out.println("Success!" + a);

                                    } else {
                                        System.out.println("Failure!" + a);
                                    }
//                                    class MyFunction {
//
//                                        public static void main(String[] args) {
//                                            // Example of calling the function
////                                            int result = addNumbers(5, 7);
//                                            int a = CLReader._Tag6C.GetEPC("192.168.1.116:9090", 1, eReadType.Single);
//                                            System.out.println("Result of adding numbers: " + a);
//
//                                        }
//
//                                        // Function to add two numbers
//
//                                    }

                                }
                            }
                        } else {
                            System.out.println("final else block");
                        }
                    }
                }


            }

        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
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