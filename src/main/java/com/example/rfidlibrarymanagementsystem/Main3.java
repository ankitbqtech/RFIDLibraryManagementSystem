package com.example.rfidlibrarymanagementsystem;
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
import com.jamierf.rxtx.RXTXLoader;

public class Main3 implements IAsynchronousMessage,ISearchDevice {

    public static void main(String[] args)
    {
        try
        {
            RXTXLoader.load();		//add library about rxtx
        }
        catch(Exception ex)
        {
        }

        Scanner sc = new Scanner(System.in);

        String readKey;
        Main3 example = new Main3();
        String ConnID = "";

        while(true)
        {
            System.out.println("Please select connect type: 1.RS232 2.TCP 3.RS485 q.Quit\n");
            readKey = sc.next();
            if(readKey.equals("1"))
            {
                System.out.println("Please input RS232 ConnID,Format: 'COM Number':'Baud Rate' like \"COM1:115200\" \n");
                ConnID = sc.next();
                if(CLReader.CreateSerialConn(ConnID, example))
                {
                    System.out.println("Connect success!\n");
                    break;
                }
                else
                {
                    System.out.println("Connect failure!\n");
                    continue;
                }
            }
            else if(readKey.equals("2"))
            {
                System.out.println("Please input TCP ConnID,Format: 'IP Address':'Connect Port' like \"192.168.1.116:9090\" \n");
                ConnID = sc.next();
                if(CLReader.CreateTcpConn(ConnID, example))
                {
                    System.out.println("Connect success!\n");
                    break;
                }
                else
                {
                    System.out.println("Connect failure!\n");
                    continue;
                }
            }
            else if(readKey.equals("3"))
            {
                System.out.println("Please input RS485 ConnID,Format: '485 Address':'COM Number':'Baud Rate' like \"1:COM1:115200\" \n");
                ConnID = sc.next();
                if(CLReader.Create485Conn(ConnID, example))
                {
                    System.out.println("Connect success!\n");
                    break;
                }
                else
                {
                    System.out.println("Connect failure!\n");
                    continue;
                }
            }
            else if (readKey.equals("q"))
            {
                CLReader.Stop(ConnID);
                CLReader.CloseConn(ConnID);
                return;
            }
            else
            {
                System.out.println("Select Parameter Error!\n");
            }
        }


        try
        {
            while(true)
            {
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

                if (readKey.equals("1"))
                {
                    while (true)
                    {
                        System.out.println("Current ConnID : " + ConnID + "\n");
                        System.out.println("Please select operation:");
                        System.out.println("1.Close Single Connect");
                        System.out.println("2.Close All Connect");
                        System.out.println("b.Back to Previous Menu");
                        System.out.println("q.Quit");

                        readKey = sc.next();
                        if (readKey.equals("1"))
                        {
                            System.out.println("Please input ConnID");
                            readKey = sc.next();
                            CLReader.CloseConn(readKey);
                            if (readKey.equals(ConnID))
                            {
                                return;
                            }
                        }
                        else if (readKey.equals("2"))
                        {
                            CLReader.CloseAllConnect();
                            return;
                        }
                        else if (readKey.equals("b"))
                        {
                            break;
                        }
                        else if (readKey.equals("q"))
                        {
                            CLReader.Stop(ConnID);
                            CLReader.CloseConn(ConnID);
                            return;
                        }
                    }
                }


                else if (readKey.equals("2"))
                {
                    while (true)
                    {
                        System.out.println("Current ConnID : " + ConnID);
                        System.out.println("Please select operation:");
                        System.out.println("1.Set IP Setting");
                        System.out.println("2.Get IP Setting");
                        System.out.println("3.Stop Read");
                        System.out.println("4.Set Reader Time");
                        System.out.println("5.Get Reader Time");
                        System.out.println("6.Set Serial Port");
                        System.out.println("7.Get Serial Port");
                        System.out.println("8.Set MAC");
                        System.out.println("9.Get MAC");
                        System.out.println("10.Set RS485");
                        System.out.println("11.Get RS485");
                        System.out.println("12.Set Server/Client");
                        System.out.println("13.Get Server/Client");
                        System.out.println("14.Reader Information");
                        System.out.println("15.Baseband Version");
                        System.out.println("b.Back to Previous Menu");
                        System.out.println("q.Quit");

                        readKey = sc.next();
                        if (readKey.equals("1"))
                        {
                            System.out.println("Please Input IP like \"192.168.1.116\"");
                            String IP = sc.next();
                            System.out.println("Please input MASK like \"255.255.255.0\"");
                            String Mask = sc.next();
                            System.out.println("Please input Gateway like \"192.168.1.1\"");
                            String Gateway = sc.next();
                            if (UHFConifg.SetReaderNetworkPortParam(ConnID, IP, Mask, Gateway) == 0)
                            {
                                System.out.println("Success!");
                                System.out.println("It is must be restart console program!");
                            }
                            else
                            {
                                System.out.println("Failure!");
                            }
                        }
                        else if (readKey.equals("2"))
                        {
                            String Result = CLReader._Config.GetReaderNetworkPortParam(ConnID);
                            System.out.println(Result);
                            System.out.println("Tip:	'IP'|'Mask'|'Gateway'");
                        }
                        else if (readKey.equals("3"))
                        {
                            if (CLReader._Config.Stop(ConnID) == 0)
                            {
                                System.out.println("Success!");
                            }
                            else
                            {
                                System.out.println("Failure!");
                            }
                        }
                        else if (readKey.equals("4"))
                        {
                            System.out.println("Please input param: \n");
                            System.out.println("Tip:	yyyy-MM-dd&HH:mm:ss	\n");
                            System.out.println("Example:	2017-04-05&12:34:56	 \n");
                            String param = sc.next();

                            if(CLReader._Config.SetReaderUTC(ConnID,param) == 0)
                            {
                                System.out.println("Success!");
                            }
                            else
                            {
                                System.out.println("Failure!");
                            }


                        }
                        else if (readKey.equals("5"))
                        {
                            String Result = CLReader._Config.GetReaderUTC(ConnID);

                            System.out.println(Result + "\n");
                        }
                        else if (readKey.equals("6"))
                        {
                            System.out.println("Please select operation:");
                            System.out.println("1.eBaudrate._9600bps");
                            System.out.println("2.eBaudrate._19200bps");
                            System.out.println("3.eBaudrate._115200bps");
                            System.out.println("4.eBaudrate._230400bps");
                            System.out.println("5.eBaudrate._460800bps");
                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                System.out.println(CLReader._Config.SetReaderSerialPortParam(ConnID, eBaudrate._9600bps));
                            }
                            else if (readKey.equals("2"))
                            {
                                System.out.println(CLReader._Config.SetReaderSerialPortParam(ConnID, eBaudrate._19200bps));
                            }
                            else if (readKey.equals("3"))
                            {
                                System.out.println(CLReader._Config.SetReaderSerialPortParam(ConnID, eBaudrate._115200bps));
                            }
                            else if (readKey.equals("4"))
                            {
                                System.out.println(CLReader._Config.SetReaderSerialPortParam(ConnID, eBaudrate._230400bps));
                            }
                            else if (readKey.equals("5"))
                            {
                                System.out.println(CLReader._Config.SetReaderSerialPortParam(ConnID, eBaudrate._460800bps));
                            }
                        }
                        else if (readKey.equals("7"))
                        {
                            System.out.println(CLReader._Config.GetReaderSerialPortParam2(ConnID));
                        }
                        else if (readKey.equals("8"))
                        {
                            System.out.println("Please input MAC:");
                            System.out.println("Tip:	00-00-00-00-00-00");
                            String param = sc.next();
                            int Result = CLReader._Config.SetReaderMacParam(ConnID, param);
                            System.out.println(Result);
                        }
                        else if (readKey.equals("9"))
                        {
                            String Result = CLReader._Config.GetReaderMacParam(ConnID);
                            System.out.println(Result);
                        }
                        else if (readKey.equals("10"))
                        {
                            System.out.println("Please input 485 address:");
                            System.out.println("Tip:	'485 address'");
                            System.out.println("Example:	1");
                            String param = sc.next();
                            int Result = CLReader._Config.SetReader485(ConnID, param);
                            System.out.println(Result);
                        }
                        else if (readKey.equals("11"))
                        {
                            String Result = CLReader._Config.GetReader485(ConnID);
                            System.out.println(Result);
                            System.out.println("Tip:	'485 address'");
                        }
                        else if (readKey.equals("12"))
                        {
                            System.out.println("Please select workMode:");
                            System.out.println("1.eWorkMode.Server");
                            System.out.println("2.eWorkMode.Client");
                            readKey = sc.next();
                            eWorkMode workMode;
                            if (readKey.equals("1"))
                            {
                                workMode = eWorkMode.Server;
                            }
                            else
                            {
                                workMode = eWorkMode.Client;
                            }

                            System.out.println("Please input IP:");
                            String IP = sc.next();
                            System.out.println("Please input Port:");
                            String Port = sc.next();
                            System.out.println(CLReader._Config.SetReaderServerOrClient(ConnID, workMode, IP, Port));
                        }
                        else if (readKey.equals("13"))
                        {
                            System.out.println(CLReader._Config.GetReaderServerOrClient(ConnID));
                            System.out.println("Tips: Server|\"Server Port\"  or  Client|\"Client IP\"|\"Client Port\"");
                        }
                        else if (readKey.equals("14"))
                        {
                            System.out.println(CLReader._Config.GetReaderInformation(ConnID));
                            System.out.println("Format: 'Application Software Version'|'Reader Name'|'Reader Power On Time'");
                            System.out.println("Tip: The unit of Reader Power On Time is second");
                        }
                        else if (readKey.equals("15"))
                        {
                            String BasebandVersion = CLReader._Config.GetReaderBaseBandSoftVersion(ConnID);
                            System.out.println(BasebandVersion + "\n");
                        }
                        else if (readKey.equals("b"))
                        {
                            break;
                        }
                        else if (readKey.equals("q"))
                        {
                            CLReader.Stop(ConnID);
                            CLReader.CloseConn(ConnID);
                            return;
                        }
                    }

                }


                else if (readKey.equals("3"))
                {
                    while (true)
                    {
                        System.out.println("Current ConnID : " + ConnID + "\n");
                        System.out.println("Please select operation:");
                        System.out.println("1.Set EPC Baseband");
                        System.out.println("2.Get EPC Baseband");
                        System.out.println("3.Set ANT Power");
                        System.out.println("4.Get ANT Power");
                        System.out.println("5.Set Tag Update");
                        System.out.println("6.Get Tag Update");
                        System.out.println("7.Get Reader Property");
                        System.out.println("8.Set Reader RF");
                        System.out.println("9.Get Reader RF");
                        System.out.println("10.Set Reader AutoSleep");
                        System.out.println("11.Get Reader AutoSleep");
                        System.out.println("12.Set ANT Enable");
                        System.out.println("13.Get ANT Enable");
                        System.out.println("b.Back to Previous Menu");
                        System.out.println("q.Quit");

                        readKey = sc.next();
                        if (readKey.equals("1"))
                        {
                            System.out.println("Please input basebandMode:");
                            System.out.println("0:Tari=25us��FM0��LHF=40KHz");
                            System.out.println("1:Tari=25us��Miller4��LHF=250KHz");
                            System.out.println("2:Tari=25us��Miller4��LHF=300KH");
                            System.out.println("3:Tari=6.25us��FM0��LHF=400KHz");
                            System.out.println("255:Auto");
                            int basebandMode = Integer.valueOf(sc.next());


                            System.out.println("Please input qValue:");
                            System.out.println("0~15");
                            int qValue = Integer.valueOf(sc.next());

                            System.out.println("Please input session:");
                            System.out.println("0~3");
                            int session = Integer.valueOf(sc.next());

                            System.out.println("Please input SearchType:");
                            System.out.println("0:Single Flag");
                            System.out.println("1:Flag B");
                            System.out.println("2:Flag A&B");
                            int SearchType = Integer.valueOf(sc.next());

                            int Result = CLReader._Config.SetEPCBaseBandParam(ConnID, basebandMode, qValue, session, SearchType);
                            if (Result == 0)
                            {
                                System.out.println("Success!");
                            }
                            else
                            {
                                System.out.println("Failure!");
                            }
                        }
                        else if (readKey.equals("2"))
                        {
                            String Result = CLReader._Config.GetEPCBaseBandParam(ConnID);
                            System.out.println("basebandMode|qValue|session|searchType");
                            System.out.println("basebandMode:");
                            System.out.println("0:Tari=25us��FM0��LHF=40KHz");
                            System.out.println("1:Tari=25us��Miller4��LHF=250KHz");
                            System.out.println("2:Tari=25us��Miller4��LHF=300KH");
                            System.out.println("3:Tari=6.25us��FM0��LHF=400KHz");
                            System.out.println("255:Auto");
                            System.out.println("searchType:");
                            System.out.println("0:Single Flag");
                            System.out.println("1:Flag B");
                            System.out.println("2:Flag A&B");
                            System.out.println(Result);
                        }
                        else if (readKey.equals("3"))
                        {
                            HashMap<Integer, Integer> AntPower = new HashMap<Integer, Integer>();
                            while (true)
                            {
                                System.out.println("Please input antenna number:");
                                int Num = Integer.valueOf(sc.next());
                                System.out.println("Please input this antenna's power (dB) ");
                                int Power = Integer.valueOf(sc.next());

                                AntPower.put(Num, Power);

                                System.out.println("Setting another antenna power");
                                System.out.println("1.Yes");
                                System.out.println("2.No");

                                readKey = sc.next();
                                if (readKey.equals("1"))
                                {
                                    continue;
                                }
                                else if (readKey.equals("2"))
                                {
                                    break;
                                }
                                else
                                {
                                    System.out.println("Parameter error ,please reset");
                                    AntPower = new HashMap<Integer, Integer>();
                                }
                            }

                            int Result = CLReader._Config.SetANTPowerParam(ConnID, AntPower);
                            if (Result == 0)
                            {
                                System.out.println("Success!");
                            }
                            else
                            {
                                System.out.println("Failure!");
                            }
                        }
                        else if (readKey.equals("4"))
                        {
                            String Result = CLReader._Config.GetANTPowerParam2(ConnID);
                            System.out.println(Result);
                            System.out.println("1,\"ANT1 Power\"&2,\"ANT2 Power\"&3,\"ANT3 Power\"&4,\"ANT4 Power\"&5,\"ANT5 Power\"&6,\"ANT6 Power\"&7,\"ANT7 Power\"&8,\"ANT8 Power\"");
                        }
                        else if (readKey.equals("5"))
                        {
                            System.out.println("Please input repeatTimeFilter:");
                            int repeatTimeFilter = Integer.valueOf(sc.next());
                            System.out.println("Please input RSSIFilter:");
                            int RSSIFilter = Integer.valueOf(sc.next());
                            int Result = CLReader._Config.SetTagUpdateParam(ConnID, repeatTimeFilter, RSSIFilter);
                            if (Result == 0)
                            {
                                System.out.println("Success!");
                            }
                            else
                            {
                                System.out.println("Failure!");
                            }
                        }
                        else if (readKey.equals("6"))
                        {
                            String Result = CLReader._Config.GetTagUpdateParam(ConnID);
                            System.out.println(Result);
                            System.out.println("repeatTimeFilter|RSSIFilter");
                            System.out.println("the unit of repeatTimeFilter is 10ms");
                        }
                        else if (readKey.equals("7"))
                        {
                            String Result = CLReader._Config.GetReaderProperty(ConnID);
                            System.out.println(Result);
                            System.out.println("Minimum Power|Maximum Power|Ant Number|RF list|RFID protocol list");
                        }
                        else if (readKey.equals("8"))
                        {
                            System.out.println("Please select eRF_Range:");
                            System.out.println("1.eRF_Range.GB_920_to_925MHz");
                            System.out.println("2.eRF_Range.GB_840_to_845MHz");
                            System.out.println("3.eRF_Range.GB_920_to_925MHz_and_GB_840_to_845MHz");
                            System.out.println("4.eRF_Range.FCC_902_to_928MHz");
                            System.out.println("5.eRF_Range.ETSI_866_to_868MHz");
                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                System.out.println(CLReader._Config.SetReaderRF(ConnID, eRF_Range.GB_920_to_925MHz));
                            }
                            else if (readKey.equals("2"))
                            {
                                System.out.println(CLReader._Config.SetReaderRF(ConnID, eRF_Range.GB_840_to_845MHz));
                            }
                            else if (readKey.equals("3"))
                            {
                                System.out.println(CLReader._Config.SetReaderRF(ConnID, eRF_Range.GB_920_to_925MHz_and_GB_840_to_845MHz));
                            }
                            else if (readKey.equals("4"))
                            {
                                System.out.println(CLReader._Config.SetReaderRF(ConnID, eRF_Range.FCC_902_to_928MHz));
                            }
                            else if (readKey.equals("5"))
                            {
                                System.out.println(CLReader._Config.SetReaderRF(ConnID, eRF_Range.ETSI_866_to_868MHz));
                            }
                        }
                        else if (readKey.equals("9"))
                        {
                            System.out.println(CLReader._Config.GetReaderRF2(ConnID));
                        }
                        else if (readKey.equals("10"))
                        {
                            System.out.println("Please select switch: ");
                            System.out.println("1.Open");
                            System.out.println("2.Close");
                            readKey = sc.next();
                            Boolean Switch;
                            if (readKey.equals("1"))
                            {
                                Switch = true;
                            }
                            else if (readKey.equals("2"))
                            {
                                Switch = false;
                            }
                            else
                            {
                                System.out.println("Parameter error!");
                                continue;
                            }

                            System.out.println("Please input time: ");
                            System.out.println("Unit:    10ms");
                            String time = sc.next();

                            System.out.println(CLReader._Config.SetReaderAutoSleepParam(ConnID, Switch, time));
                        }
                        else if (readKey.equals("11"))
                        {
                            System.out.println(CLReader._Config.GetReaderAutoSleepParam(ConnID));
                            System.out.println("Unit:    10ms");
                        }
                        else if (readKey.equals("12"))
                        {
                            int antennaNo = 0;
                            while (true)
                            {
                                System.out.println("Please input antenna number:");
                                readKey = sc.next();
                                if (readKey.equals("1"))
                                {
                                    antennaNo += eAntennaNo._1.GetNum();
                                }
                                else if (readKey.equals("2"))
                                {
                                    antennaNo += eAntennaNo._2.GetNum();
                                }
                                else if (readKey.equals("3"))
                                {
                                    antennaNo += eAntennaNo._3.GetNum();
                                }
                                else if (readKey.equals("4"))
                                {
                                    antennaNo += eAntennaNo._4.GetNum();
                                }
                                else if (readKey.equals("5"))
                                {
                                    antennaNo += eAntennaNo._5.GetNum();
                                }
                                else if (readKey.equals("6"))
                                {
                                    antennaNo += eAntennaNo._6.GetNum();
                                }
                                else if (readKey.equals("7"))
                                {
                                    antennaNo += eAntennaNo._7.GetNum();
                                }
                                else if (readKey.equals("8"))
                                {
                                    antennaNo += eAntennaNo._8.GetNum();
                                }
                                else
                                {
                                    System.out.println("antenna number error");
                                    continue;
                                }

                                System.out.println("Setting another antenna enable");
                                System.out.println("1.Yes");
                                System.out.println("2.No");
                                readKey = sc.next();
                                if (readKey.equals("1"))
                                {
                                    continue;
                                }
                                else if (readKey.equals("2"))
                                {
                                    break;
                                }
                                else
                                {
                                    System.out.println("Parameter error ,please reset");
                                    antennaNo = 0;
                                }
                            }

                            System.out.println(CLReader._Config.SetReaderANT(ConnID, antennaNo));
                        }
                        else if (readKey.equals("13"))
                        {
                            System.out.println(CLReader._Config.GetReaderANT2(ConnID));
                        }
                        else if (readKey.equals("b"))
                        {
                            break;
                        }
                        else if (readKey.equals("q"))
                        {
                            CLReader.Stop(ConnID);
                            CLReader.CloseConn(ConnID);
                            return;
                        }

                    }

                }


                else if (readKey.equals("4"))
                {
                    while (true)
                    {
                        System.out.println("Current ConnID : " + ConnID + "\n");
                        System.out.println("Please select operation:");
                        System.out.println("1.Set GPI");
                        System.out.println("2.Get GPI");
                        System.out.println("3.Get GPI State");
                        System.out.println("4.Set GPO");
                        System.out.println("5.Set Wiegand");
                        System.out.println("6.Get Wiegand");
                        System.out.println("b.Back to Previous Menu");
                        System.out.println("q.Quit");

                        readKey = sc.next();
                        if (readKey.equals("1"))
                        {
                            System.out.println("Please select GPINum");
                            System.out.println("1.eGPI._1");
                            System.out.println("2.eGPI._2");
                            System.out.println("3.eGPI._3");
                            System.out.println("4.eGPI._4");
                            eGPI GPINum = null;

                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                GPINum = eGPI._1;
                            }
                            else if (readKey.equals("2"))
                            {
                                GPINum = eGPI._2;
                            }
                            else if (readKey.equals("3"))
                            {
                                GPINum = eGPI._3;
                            }
                            else if (readKey.equals("4"))
                            {
                                GPINum = eGPI._4;
                            }

                            System.out.println("Please select triggerStart");
                            System.out.println("1.eTriggerStart.OFF");
                            System.out.println("2.eTriggerStart.Low_level");
                            System.out.println("3.TriggerStart.High_level");
                            System.out.println("4.eTriggerStart.Rising_edge");
                            System.out.println("5.eTriggerStart.Falling_edge");
                            System.out.println("6.eTriggerStart.Any_edge");
                            eTriggerStart triggerStart = null;

                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                triggerStart = eTriggerStart.OFF;
                            }
                            else if (readKey.equals("2"))
                            {
                                triggerStart = eTriggerStart.Low_level;
                            }
                            else if (readKey.equals("3"))
                            {
                                triggerStart = eTriggerStart.High_level;
                            }
                            else if (readKey.equals("4"))
                            {
                                triggerStart = eTriggerStart.Rising_edge;
                            }
                            else if (readKey.equals("5"))
                            {
                                triggerStart = eTriggerStart.Falling_edge;
                            }
                            else if (readKey.equals("6"))
                            {
                                triggerStart = eTriggerStart.Any_edge;
                            }

                            System.out.println("Please select triggerCode");
                            System.out.println("1.triggerCode.Single_Antenna_read_EPC");
                            System.out.println("2.triggerCode.Single_Antenna_read_EPC_and_TID");
                            System.out.println("3.triggerCode.Double_Antenna_read_EPC");
                            System.out.println("4.triggerCode.Double_Antenna_read_EPC_and_TID");
                            System.out.println("5.triggerCode.Four_Antenna_read_EPC");
                            System.out.println("6.triggerCode.Four_Antenna_read_EPC_and_TID");

                            eTriggerCode triggerCode = null;

                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                triggerCode = eTriggerCode.Single_Antenna_read_EPC;
                            }
                            else if (readKey.equals("2"))
                            {
                                triggerCode = eTriggerCode.Single_Antenna_read_EPC_and_TID;
                            }
                            else if (readKey.equals("3"))
                            {
                                triggerCode = eTriggerCode.Double_Antenna_read_EPC;
                            }
                            else if (readKey.equals("4"))
                            {
                                triggerCode = eTriggerCode.Double_Antenna_read_EPC_and_TID;
                            }
                            else if (readKey.equals("5"))
                            {
                                triggerCode = eTriggerCode.Four_Antenna_read_EPC;
                            }
                            else if (readKey.equals("6"))
                            {
                                triggerCode = eTriggerCode.Four_Antenna_read_EPC_and_TID;
                            }

                            System.out.println("Please select triggerStop");
                            System.out.println("1.eTriggerStart.OFF");
                            System.out.println("2.eTriggerStart.Low_level");
                            System.out.println("3.TriggerStart.High_level");
                            System.out.println("4.eTriggerStart.Rising_edge");
                            System.out.println("5.eTriggerStart.Falling_edge");
                            System.out.println("6.eTriggerStart.Any_edge");
                            System.out.println("7.eTriggerStart.Delay");
                            eTriggerStop triggerStop = null;

                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                triggerStop = eTriggerStop.OFF;
                            }
                            else if (readKey.equals("2"))
                            {
                                triggerStop = eTriggerStop.Low_level;
                            }
                            else if (readKey.equals("3"))
                            {
                                triggerStop = eTriggerStop.High_level;
                            }
                            else if (readKey.equals("4"))
                            {
                                triggerStop = eTriggerStop.Rising_edge;
                            }
                            else if (readKey.equals("5"))
                            {
                                triggerStop = eTriggerStop.Falling_edge;
                            }
                            else if (readKey.equals("6"))
                            {
                                triggerStop = eTriggerStop.Any_edge;
                            }
                            else if (readKey.equals("7"))
                            {
                                triggerStop = eTriggerStop.Delay;
                            }

                            String DelayTime = "";

                            if (triggerStop == eTriggerStop.Delay)
                            {
                                System.out.println("Please input delaytime, unit:10ms");
                                DelayTime = sc.next();
                            }

                            System.out.println(CLReader._Config.SetReaderGPIParam(ConnID, GPINum, triggerStart, triggerCode, triggerStop, DelayTime));

                        }
                        else if (readKey.equals("2"))
                        {
                            System.out.println("Please select GPINum");
                            System.out.println("1.eGPI._1");
                            System.out.println("2.eGPI._2");
                            System.out.println("3.eGPI._3");
                            System.out.println("4.eGPI._4");
                            eGPI GPINum;

                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                GPINum = eGPI._1;
                            }
                            else if (readKey.equals("2"))
                            {
                                GPINum = eGPI._2;
                            }
                            else if (readKey.equals("3"))
                            {
                                GPINum = eGPI._3;
                            }
                            else if (readKey.equals("4"))
                            {
                                GPINum = eGPI._4;
                            }
                            else
                            {
                                return;
                            }

                            System.out.println(CLReader._Config.GetReaderGPIParam(ConnID, GPINum));
                            System.out.println("TriggerStart|TriggerCode|TriggerStop|TimeDelay, unit:10ms");
                        }
                        else if (readKey.equals("3"))
                        {
                            System.out.println(CLReader._Config.GetReaderGPIState(ConnID));
                            System.out.println("1,\"GPI1 State\" & 2,\"GPI2 State\" & 3,\"GPI3 State\" & 4,\"GPI4 State\"");
                        }
                        else if (readKey.equals("4"))
                        {
                            HashMap<eGPO, eGPOState> dicState = new HashMap<eGPO, eGPOState>();
                            while (true)
                            {
                                System.out.println("Please select GPO number:");
                                System.out.println("1.GPO._1");
                                System.out.println("2.GPO._2");
                                System.out.println("3.GPO._3");
                                System.out.println("4.GPO._4");

                                eGPO GPO;

                                readKey = sc.next();
                                if (readKey.equals("1"))
                                {
                                    GPO = eGPO._1;
                                }
                                else if (readKey.equals("2"))
                                {
                                    GPO = eGPO._2;
                                }
                                else if (readKey.equals("3"))
                                {
                                    GPO = eGPO._3;
                                }
                                else if (readKey.equals("4"))
                                {
                                    GPO = eGPO._4;
                                }
                                else
                                {
                                    return;
                                }


                                System.out.println("Please select GPO state ");
                                System.out.println("1.eGPOState.Low");
                                System.out.println("2.eGPOState.High");

                                eGPOState GPOState;

                                readKey = sc.next();
                                if (readKey.equals("1"))
                                {
                                    GPOState = eGPOState.Low;
                                }
                                else if (readKey.equals("2"))
                                {
                                    GPOState = eGPOState._High;
                                }
                                else
                                {
                                    return;
                                }

                                dicState.put(GPO, GPOState);

                                System.out.println("Setting another GPO state");
                                System.out.println("1.Yes");
                                System.out.println("2.No");
                                readKey = sc.next();
                                if (readKey.equals("1"))
                                {
                                    continue;
                                }
                                else if (readKey.equals("2"))
                                {
                                    break;
                                }
                                else
                                {
                                    System.out.println("Parameter error ,please reset");
                                    dicState = new HashMap<eGPO, eGPOState>();
                                }
                            }

                            int Result = CLReader._Config.SetReaderGPOState(ConnID, dicState);
                            if (Result == 0)
                            {
                                System.out.println("Success!");
                            }
                            else
                            {
                                System.out.println("Failure!");
                            }
                        }
                        else if (readKey.equals("5"))
                        {
                            System.out.println("Please select wiegandSwitch ");
                            System.out.println("1.eWiegandSwitch.Close");
                            System.out.println("2.eWiegandSwitch.Open");

                            eWiegandSwitch wiegandSwitch;

                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                wiegandSwitch = eWiegandSwitch.Close;
                            }
                            else if (readKey.equals("2"))
                            {
                                wiegandSwitch = eWiegandSwitch.Open;
                            }
                            else
                            {
                                return;
                            }

                            System.out.println("Please select wiegandFormat ");
                            System.out.println("1.eWiegandFormat.Wiegand26");
                            System.out.println("2.eWiegandFormat.Wiegand34");
                            System.out.println("3.eWiegandFormat.Wiegand66");

                            eWiegandFormat wiegandFormat;
                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                wiegandFormat = eWiegandFormat.Wiegand26;
                            }
                            else if (readKey.equals("2"))
                            {
                                wiegandFormat = eWiegandFormat.Wiegand34;
                            }
                            else if (readKey.equals("3"))
                            {
                                wiegandFormat = eWiegandFormat.Wiegand66;
                            }
                            else
                            {
                                return;
                            }

                            System.out.println("Please select wiegandDetails ");
                            System.out.println("1.eWiegandDetails.end_of_the_EPC_data");
                            System.out.println("2.eWiegandDetails.end_of_the_TID_data");

                            eWiegandDetails wiegandDetails;

                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                wiegandDetails = eWiegandDetails.end_of_the_EPC_data;
                            }
                            else if (readKey.equals("2"))
                            {
                                wiegandDetails = eWiegandDetails.end_of_the_TID_data;
                            }
                            else
                            {
                                return;
                            }

                            System.out.println(CLReader._Config.SetReaderWG(ConnID, wiegandSwitch, wiegandFormat, wiegandDetails));

                        }
                        else if (readKey.equals("6"))
                        {
                            System.out.println(CLReader._Config.GetReaderWG(ConnID));
                        }
                        else if (readKey.equals("b"))
                        {
                            break;
                        }
                        else if (readKey.equals("q"))
                        {
                            CLReader.Stop(ConnID);
                            CLReader.CloseConn(ConnID);
                            return;
                        }
                    }
                }


                else if (readKey.equals("5"))
                {
                    while (true)
                    {
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



                        if (readKey.equals("1"))
                        {
                            while (true)
                            {
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

                                if (readKey.equals("1"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    if (CLReader._Tag6C.GetEPC(ConnID, antNum, readType) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }

                                }
                                else if (readKey.equals("2"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC(ConnID, antNum, readType, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }

                                }
                                else if (readKey.equals("3"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    if (CLReader._Tag6C.GetEPC_MatchEPC(ConnID, antNum, readType, sEPC) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("4"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.GetEPC_MatchEPC(ConnID, antNum, readType, sEPC, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("5"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC_MatchEPC(ConnID, antNum, readType, sEPC, matchWordStartIndex, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("6"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    if (CLReader._Tag6C.GetEPC_MatchTID(ConnID, antNum, readType, sTID) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("7"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");

                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.GetEPC_MatchTID(ConnID, antNum, readType, sTID, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("8"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC_MatchTID(ConnID, antNum, readType, sTID, matchWordStartIndex, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("9"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    if (CLReader._Tag6C.GetEPC_TID(ConnID, antNum, readType) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("10"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID(ConnID, antNum, readType, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("11"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_MatchEPC(ConnID, antNum, readType, sEPC) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("12"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.GetEPC_TID_MatchEPC(ConnID, antNum, readType, sEPC, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("13"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_MatchEPC(ConnID, antNum, readType, sEPC, matchWordStartIndex, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("14"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_MatchTID(ConnID, antNum, readType, sTID) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("15"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.GetEPC_TID_MatchTID(ConnID, antNum, readType, sTID, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }

                                }
                                else if (readKey.equals("16"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_MatchTID(ConnID, antNum, readType, sTID, matchWordStartIndex, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("17"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input readStart:");
                                    int readStart = Integer.valueOf(sc.next());

                                    System.out.println("Please input readLen:");
                                    int readLen = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.GetEPC_TID_UserData(ConnID, antNum, readType, readStart, readLen) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("18"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input readStart:");
                                    int readStart = Integer.valueOf(sc.next());

                                    System.out.println("Please input readLen:");
                                    int readLen = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_UserData(ConnID, antNum, readType, readStart, readLen, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("19"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input readStart:");
                                    int readStart = Integer.valueOf(sc.next());

                                    System.out.println("Please input readLen:");
                                    int readLen = Integer.valueOf(sc.next());

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_UserData_MatchEPC(ConnID, antNum, readType, readStart, readLen, sEPC) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("20"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input readStart:");
                                    int readStart = Integer.valueOf(sc.next());

                                    System.out.println("Please input readLen:");
                                    int readLen = Integer.valueOf(sc.next());

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.GetEPC_TID_UserData_MatchEPC(ConnID, antNum, readType, readStart, readLen, sEPC, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("21"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input readStart:");
                                    int readStart = Integer.valueOf(sc.next());

                                    System.out.println("Please input readLen:");
                                    int readLen = Integer.valueOf(sc.next());

                                    System.out.println("Please input sEPC:");
                                    String sEPC = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_UserData_MatchEPC(ConnID, antNum, readType, readStart, readLen, sEPC, matchWordStartIndex, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("22"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input readStart:");
                                    int readStart = Integer.valueOf(sc.next());

                                    System.out.println("Please input readLen:");
                                    int readLen = Integer.valueOf(sc.next());

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_UserData_MatchTID(ConnID, antNum, readType, readStart, readLen, sTID) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("23"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input readStart:");
                                    int readStart = Integer.valueOf(sc.next());

                                    System.out.println("Please input readLen:");
                                    int readLen = Integer.valueOf(sc.next());

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.GetEPC_TID_UserData_MatchTID(ConnID, antNum, readType, readStart, readLen, sTID, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("24"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    eReadType readType;
                                    System.out.println("Please select ReadType:");
                                    System.out.println("1.eReadType.Single");
                                    System.out.println("2.eReadType.Inventory");
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        readType = eReadType.Single;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        readType = eReadType.Inventory;
                                    }
                                    else
                                    {
                                        System.out.println("ReadType error");
                                        continue;
                                    }

                                    System.out.println("Please input readStart:");
                                    int readStart = Integer.valueOf(sc.next());

                                    System.out.println("Please input readLen:");
                                    int readLen = Integer.valueOf(sc.next());

                                    System.out.println("Please input sTID:");
                                    String sTID = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword:");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.GetEPC_TID_UserData_MatchTID(ConnID, antNum, readType, readStart, readLen, sTID, matchWordStartIndex, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("b"))
                                {
                                    break ;
                                }
                                else if (readKey.equals("s"))
                                {
                                    CLReader._Config.Stop(ConnID);
                                }
                                else if (readKey.equals("q"))
                                {
                                    CLReader.Stop(ConnID);
                                    CLReader.CloseConn(ConnID);
                                    return;
                                }

                            }
                        }






                        else if (readKey.equals("2"))
                        {


                            while (true)
                            {

                                System.out.println("Please select operation:");
                                System.out.println("1.Write EPC");
                                System.out.println("2.Write UserData");
                                System.out.println("3.Write Password");
                                System.out.println("b.Back to Previous Menu");
                                System.out.println("q.Quit");



                                readKey = sc.next();
                                if (readKey.equals("1"))
                                {
                                    while (true)
                                    {
                                        System.out.println("Please select method:");
                                        System.out.println("1.WriteEPC(String ConnID, int antNum, String sWriteData)");
                                        System.out.println("2.WriteEPC_MatchEPC(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex) ");
                                        System.out.println("3.WriteEPC_MatchEPC(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex, String accessPassword) ");
                                        System.out.println("4.WriteEPC_MatchTID(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex) ");
                                        System.out.println("5.WriteEPC_MatchTID(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex, String accessPassword) ");
                                        System.out.println("b.Back to Previous Menu");
                                        System.out.println("q.Quit");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();


                                            if (CLReader._Tag6C.WriteEPC(ConnID, antNum, sWriteData) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }

                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            if (CLReader._Tag6C.WriteEPC_MatchEPC(ConnID, antNum, sWriteData, sMatchData, matchWordStartIndex) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            System.out.println("Please input accessPassword:");
                                            String accessPassword = sc.next();

                                            if (CLReader._Tag6C.WriteEPC_MatchEPC(ConnID, antNum, sWriteData, sMatchData, matchWordStartIndex, accessPassword) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            if (CLReader._Tag6C.WriteEPC_MatchTID(ConnID, antNum, sWriteData, sMatchData, matchWordStartIndex) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            System.out.println("Please input accessPassword:");
                                            String accessPassword = sc.next();

                                            if (CLReader._Tag6C.WriteEPC_MatchTID(ConnID, antNum, sWriteData, sMatchData, matchWordStartIndex, accessPassword) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("b"))
                                        {
                                            break;
                                        }
                                        else if (readKey.equals("q"))
                                        {
                                            CLReader.Stop(ConnID);
                                            CLReader.CloseConn(ConnID);
                                            return;
                                        }
                                    }
                                }




                                else if (readKey.equals("2"))
                                {
                                    while (true)
                                    {
                                        System.out.println("Please select method:");
                                        System.out.println("1.WriteUserData(String ConnID, int antNum, String sWriteData)");
                                        System.out.println("2.WriteUserData_MatchEPC(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex) ");
                                        System.out.println("3.WriteUserData_MatchEPC(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex, String accessPassword) ");
                                        System.out.println("4.WriteUserData_MatchTID(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex) ");
                                        System.out.println("5.WriteUserData_MatchTID(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex, String accessPassword) ");
                                        System.out.println("b.Back to Previous Menu");
                                        System.out.println("q.Quit");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();


                                            if (CLReader._Tag6C.WriteUserData(ConnID, antNum, sWriteData,0) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }

                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            if (CLReader._Tag6C.WriteUserData_MatchEPC(ConnID, antNum, sWriteData,0, sMatchData, matchWordStartIndex) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            System.out.println("Please input accessPassword:");
                                            String accessPassword = sc.next();

                                            if (CLReader._Tag6C.WriteUserData_MatchEPC(ConnID, antNum, sWriteData,0, sMatchData, matchWordStartIndex, accessPassword) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            if (CLReader._Tag6C.WriteUserData_MatchTID(ConnID, antNum, sWriteData,0, sMatchData, matchWordStartIndex) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            System.out.println("Please input accessPassword:");
                                            String accessPassword = sc.next();

                                            if (CLReader._Tag6C.WriteUserData_MatchTID(ConnID, antNum, sWriteData,0, sMatchData, matchWordStartIndex, accessPassword) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("b"))
                                        {
                                            break;
                                        }
                                        else if (readKey.equals("q"))
                                        {
                                            CLReader.Stop(ConnID);
                                            CLReader.CloseConn(ConnID);
                                            return;
                                        }
                                    }
                                }





                                else if (readKey.equals("3"))
                                {
                                    while (true)
                                    {
                                        System.out.println("Please select method:");
                                        System.out.println("1.WriteAccessPassWord(String ConnID, int antNum, String sWriteData) ");
                                        System.out.println("2.WriteAccessPassWord(String ConnID, int antNum, String sWriteData, String accessPassword) ");
                                        System.out.println("3.WriteAccessPassWord_MatchTID(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex, String accessPassword)");
                                        System.out.println("4.WriteDestroyPassWord(String ConnID, int antNum, String sWriteData) ");
                                        System.out.println("5.WriteDestroyPassWord(String ConnID, int antNum, String sWriteData, String accessPassword) ");
                                        System.out.println("6.WriteDestroyPassWord_MatchTID(String ConnID, int antNum, String sWriteData, String sMatchData, int matchWordStartIndex, String accessPassword)");
                                        System.out.println("b.Back to Previous Menu");
                                        System.out.println("q.Quit");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();


                                            if (CLReader._Tag6C.WriteAccessPassWord(ConnID, antNum, sWriteData) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input accessPassword:");
                                            String accessPassword = sc.next();


                                            if (CLReader._Tag6C.WriteAccessPassWord(ConnID, antNum, sWriteData, accessPassword) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            System.out.println("Please input accessPassword:");
                                            String accessPassword = sc.next();

                                            if (CLReader._Tag6C.WriteAccessPassWord_MatchTID(ConnID, antNum, sWriteData, sMatchData, matchWordStartIndex, accessPassword) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();


                                            if (CLReader._Tag6C.WriteDestroyPassWord(ConnID, antNum, sWriteData) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input accessPassword:");
                                            String accessPassword = sc.next();


                                            if (CLReader._Tag6C.WriteDestroyPassWord(ConnID, antNum, sWriteData, accessPassword) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            int antNum = 0;
                                            while (true)
                                            {
                                                System.out.println("Please input antenna number:");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    antNum += eAntennaNo._1.GetNum();
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    antNum += eAntennaNo._2.GetNum();
                                                }
                                                else if (readKey.equals("3"))
                                                {
                                                    antNum += eAntennaNo._3.GetNum();
                                                }
                                                else if (readKey.equals("4"))
                                                {
                                                    antNum += eAntennaNo._4.GetNum();
                                                }
                                                else if (readKey.equals("5"))
                                                {
                                                    antNum += eAntennaNo._5.GetNum();
                                                }
                                                else if (readKey.equals("6"))
                                                {
                                                    antNum += eAntennaNo._6.GetNum();
                                                }
                                                else if (readKey.equals("7"))
                                                {
                                                    antNum += eAntennaNo._7.GetNum();
                                                }
                                                else if (readKey.equals("8"))
                                                {
                                                    antNum += eAntennaNo._8.GetNum();
                                                }
                                                else
                                                {
                                                    System.out.println("antenna number error");
                                                    continue;
                                                }

                                                System.out.println("Setting another antenna");
                                                System.out.println("1.Yes");
                                                System.out.println("2.No");
                                                readKey = sc.next();
                                                if (readKey.equals("1"))
                                                {
                                                    continue;
                                                }
                                                else if (readKey.equals("2"))
                                                {
                                                    break;
                                                }
                                                else
                                                {
                                                    System.out.println("Parameter error ,please reset");
                                                    antNum = 0;
                                                }
                                            }

                                            System.out.println("Please input sWriteData:");
                                            String sWriteData = sc.next();

                                            System.out.println("Please input sMatchData:");
                                            String sMatchData = sc.next();

                                            System.out.println("Please input matchWordStartIndex:");
                                            int matchWordStartIndex = Integer.valueOf(sc.next());

                                            System.out.println("Please input accessPassword:");
                                            String accessPassword = sc.next();

                                            if (CLReader._Tag6C.WriteDestroyPassWord_MatchTID(ConnID, antNum, sWriteData, sMatchData, matchWordStartIndex, accessPassword) == 0)
                                            {
                                                System.out.println("Success!");
                                            }
                                            else
                                            {
                                                System.out.println("Failure!");
                                            }
                                        }
                                        else if (readKey.equals("b"))
                                        {
                                            break;
                                        }
                                        else if (readKey.equals("q"))
                                        {
                                            CLReader.Stop(ConnID);
                                            CLReader.CloseConn(ConnID);
                                            return;
                                        }
                                    }
                                }


                                else if (readKey.equals("b"))
                                {
                                    break;
                                }
                                else if (readKey.equals("q"))
                                {
                                    CLReader.Stop(ConnID);
                                    CLReader.CloseConn(ConnID);
                                    return;
                                }
                            }
                        }





                        else if (readKey.equals("3"))
                        {
                            while (true)
                            {
                                System.out.println("Please select method:");
                                System.out.println("1.Lock(String ConnID, int antNum, eLockArea lockArea, eLockType lockType)");
                                System.out.println("2.Lock_MatchEPC(String ConnID, int antNum, eLockArea lockArea, eLockType lockType, String sMatchData, int matchWordStartIndex)");
                                System.out.println("3.Lock_MatchEPC(String ConnID, int antNum, eLockArea lockArea, eLockType lockType, String sMatchData, int matchWordStartIndex, String accessPassword)");
                                System.out.println("4.Lock_MatchTID(String ConnID, int antNum, eLockArea lockArea, eLockType lockType, String sMatchData, int matchWordStartIndex)");
                                System.out.println("5.Lock_MatchTID(String ConnID, int antNum, eLockArea lockArea, eLockType lockType, String sMatchData, int matchWordStartIndex, String accessPassword)");
                                System.out.println("b.Back to Previous Menu");
                                System.out.println("q.Quit");
                                readKey = sc.next();

                                if (readKey.equals("1"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    System.out.println("Please select lockArea:");
                                    System.out.println("1.eLockArea.epc");
                                    System.out.println("2.eLockArea.tid");
                                    System.out.println("3.eLockArea.userdata");
                                    System.out.println("4.eLockArea.AccessPassword");
                                    System.out.println("5.eLockArea.DestroyPassword");
                                    eLockArea lockArea;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockArea = eLockArea.epc;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockArea = eLockArea.tid;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockArea = eLockArea.userdata;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockArea = eLockArea.AccessPassword;
                                    }
                                    else if (readKey.equals("5"))
                                    {
                                        lockArea = eLockArea.DestroyPassword;
                                    }
                                    else
                                    {
                                        System.out.println("lockArea error");
                                        continue;
                                    }


                                    System.out.println("Please select lockType:");
                                    System.out.println("1.eLockType.Lock;");
                                    System.out.println("2.eLockType.Unlock;");
                                    System.out.println("3.eLockType.PermanentLock");
                                    System.out.println("4.eLockType.PermanentUnlock");
                                    eLockType lockType;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockType = eLockType.Lock;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockType = eLockType.Unlock;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockType = eLockType.PermanentLock;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockType = eLockType.PermanentUnlock;
                                    }
                                    else
                                    {
                                        System.out.println("lockType error");
                                        continue;
                                    }


                                    if (CLReader._Tag6C.Lock(ConnID, antNum, lockArea, lockType) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }

                                }
                                else if (readKey.equals("2"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    System.out.println("Please select lockArea:");
                                    System.out.println("1.eLockArea.epc");
                                    System.out.println("2.eLockArea.tid");
                                    System.out.println("3.eLockArea.userdata");
                                    System.out.println("4.eLockArea.AccessPassword");
                                    System.out.println("5.eLockArea.DestroyPassword");
                                    eLockArea lockArea;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockArea = eLockArea.epc;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockArea = eLockArea.tid;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockArea = eLockArea.userdata;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockArea = eLockArea.AccessPassword;
                                    }
                                    else if (readKey.equals("5"))
                                    {
                                        lockArea = eLockArea.DestroyPassword;
                                    }
                                    else
                                    {
                                        System.out.println("lockArea error");
                                        continue;
                                    }


                                    System.out.println("Please select lockType:");
                                    System.out.println("1.eLockType.Lock;");
                                    System.out.println("2.eLockType.Unlock;");
                                    System.out.println("3.eLockType.PermanentLock");
                                    System.out.println("4.eLockType.PermanentUnlock");
                                    eLockType lockType;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockType = eLockType.Lock;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockType = eLockType.Unlock;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockType = eLockType.PermanentLock;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockType = eLockType.PermanentUnlock;
                                    }
                                    else
                                    {
                                        System.out.println("lockType error");
                                        continue;
                                    }

                                    System.out.println("Please input sMatchData");
                                    String sMatchData = sc.next();

                                    System.out.println("Please input matchWordStartIndex");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.Lock_MatchEPC(ConnID, antNum, lockArea, lockType, sMatchData, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("3"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    System.out.println("Please select lockArea:");
                                    System.out.println("1.eLockArea.epc");
                                    System.out.println("2.eLockArea.tid");
                                    System.out.println("3.eLockArea.userdata");
                                    System.out.println("4.eLockArea.AccessPassword");
                                    System.out.println("5.eLockArea.DestroyPassword");
                                    eLockArea lockArea;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockArea = eLockArea.epc;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockArea = eLockArea.tid;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockArea = eLockArea.userdata;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockArea = eLockArea.AccessPassword;
                                    }
                                    else if (readKey.equals("5"))
                                    {
                                        lockArea = eLockArea.DestroyPassword;
                                    }
                                    else
                                    {
                                        System.out.println("lockArea error");
                                        continue;
                                    }


                                    System.out.println("Please select lockType:");
                                    System.out.println("1.eLockType.Lock;");
                                    System.out.println("2.eLockType.Unlock;");
                                    System.out.println("3.eLockType.PermanentLock");
                                    System.out.println("4.eLockType.PermanentUnlock");
                                    eLockType lockType;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockType = eLockType.Lock;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockType = eLockType.Unlock;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockType = eLockType.PermanentLock;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockType = eLockType.PermanentUnlock;
                                    }
                                    else
                                    {
                                        System.out.println("lockType error");
                                        continue;
                                    }

                                    System.out.println("Please input sMatchData");
                                    String sMatchData = sc.next();

                                    System.out.println("Please input matchWordStartIndex");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.Lock_MatchEPC(ConnID, antNum, lockArea, lockType, sMatchData, matchWordStartIndex, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("4"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    System.out.println("Please select lockArea:");
                                    System.out.println("1.eLockArea.epc");
                                    System.out.println("2.eLockArea.tid");
                                    System.out.println("3.eLockArea.userdata");
                                    System.out.println("4.eLockArea.AccessPassword");
                                    System.out.println("5.eLockArea.DestroyPassword");
                                    eLockArea lockArea;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockArea = eLockArea.epc;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockArea = eLockArea.tid;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockArea = eLockArea.userdata;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockArea = eLockArea.AccessPassword;
                                    }
                                    else if (readKey.equals("5"))
                                    {
                                        lockArea = eLockArea.DestroyPassword;
                                    }
                                    else
                                    {
                                        System.out.println("lockArea error");
                                        continue;
                                    }


                                    System.out.println("Please select lockType:");
                                    System.out.println("1.eLockType.Lock;");
                                    System.out.println("2.eLockType.Unlock;");
                                    System.out.println("3.eLockType.PermanentLock");
                                    System.out.println("4.eLockType.PermanentUnlock");
                                    eLockType lockType;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockType = eLockType.Lock;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockType = eLockType.Unlock;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockType = eLockType.PermanentLock;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockType = eLockType.PermanentUnlock;
                                    }
                                    else
                                    {
                                        System.out.println("lockType error");
                                        continue;
                                    }

                                    System.out.println("Please input sMatchData");
                                    String sMatchData = sc.next();

                                    System.out.println("Please input matchWordStartIndex");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.Lock_MatchEPC(ConnID, antNum, lockArea, lockType, sMatchData, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("5"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    System.out.println("Please select lockArea:");
                                    System.out.println("1.eLockArea.epc");
                                    System.out.println("2.eLockArea.tid");
                                    System.out.println("3.eLockArea.userdata");
                                    System.out.println("4.eLockArea.AccessPassword");
                                    System.out.println("5.eLockArea.DestroyPassword");
                                    eLockArea lockArea;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockArea = eLockArea.epc;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockArea = eLockArea.tid;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockArea = eLockArea.userdata;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockArea = eLockArea.AccessPassword;
                                    }
                                    else if (readKey.equals("5"))
                                    {
                                        lockArea = eLockArea.DestroyPassword;
                                    }
                                    else
                                    {
                                        System.out.println("lockArea error");
                                        continue;
                                    }


                                    System.out.println("Please select lockType:");
                                    System.out.println("1.eLockType.Lock;");
                                    System.out.println("2.eLockType.Unlock;");
                                    System.out.println("3.eLockType.PermanentLock");
                                    System.out.println("4.eLockType.PermanentUnlock");
                                    eLockType lockType;
                                    readKey = sc.next();
                                    if (readKey.equals("1"))
                                    {
                                        lockType = eLockType.Lock;
                                    }
                                    else if (readKey.equals("2"))
                                    {
                                        lockType = eLockType.Unlock;
                                    }
                                    else if (readKey.equals("3"))
                                    {
                                        lockType = eLockType.PermanentLock;
                                    }
                                    else if (readKey.equals("4"))
                                    {
                                        lockType = eLockType.PermanentUnlock;
                                    }
                                    else
                                    {
                                        System.out.println("lockType error");
                                        continue;
                                    }

                                    System.out.println("Please input sMatchData");
                                    String sMatchData = sc.next();

                                    System.out.println("Please input matchWordStartIndex");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    System.out.println("Please input accessPassword");
                                    String accessPassword = sc.next();

                                    if (CLReader._Tag6C.Lock_MatchTID(ConnID, antNum, lockArea, lockType, sMatchData, matchWordStartIndex, accessPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("b"))
                                {
                                    break;
                                }
                                else if (readKey.equals("q"))
                                {
                                    CLReader.Stop(ConnID);
                                    CLReader.CloseConn(ConnID);
                                    return;
                                }

                            }
                        }





                        else if (readKey.equals("4"))
                        {
                            while (true)
                            {
                                System.out.println("Please select method:");
                                System.out.println("1.Destroy(String ConnID, int antNum, String destroyPassword)");
                                System.out.println("2.Destroy_MatchEPC(String ConnID, int antNum, String destroyPassword, String sMatchData, int matchWordStartIndex)");
                                System.out.println("3.Destroy_MatchTID(String ConnID, int antNum, String destroyPassword, String sMatchData, int matchWordStartIndex)");
                                System.out.println("b.Back to Previous Menu");
                                System.out.println("q.Quit");
                                readKey = sc.next();
                                if (readKey.equals("1"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    System.out.println("Please input destroyPassword:");
                                    String destroyPassword = sc.next();


                                    if (CLReader._Tag6C.Destroy(ConnID, antNum, destroyPassword) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }

                                }
                                else if (readKey.equals("2"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    System.out.println("Please input destroyPassword:");
                                    String destroyPassword = sc.next();

                                    System.out.println("Please input sMatchData:");
                                    String sMatchData = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.Destroy_MatchEPC(ConnID, antNum, destroyPassword, sMatchData, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("3"))
                                {
                                    int antNum = 0;
                                    while (true)
                                    {
                                        System.out.println("Please input antenna number:");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            antNum += eAntennaNo._1.GetNum();
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            antNum += eAntennaNo._2.GetNum();
                                        }
                                        else if (readKey.equals("3"))
                                        {
                                            antNum += eAntennaNo._3.GetNum();
                                        }
                                        else if (readKey.equals("4"))
                                        {
                                            antNum += eAntennaNo._4.GetNum();
                                        }
                                        else if (readKey.equals("5"))
                                        {
                                            antNum += eAntennaNo._5.GetNum();
                                        }
                                        else if (readKey.equals("6"))
                                        {
                                            antNum += eAntennaNo._6.GetNum();
                                        }
                                        else if (readKey.equals("7"))
                                        {
                                            antNum += eAntennaNo._7.GetNum();
                                        }
                                        else if (readKey.equals("8"))
                                        {
                                            antNum += eAntennaNo._8.GetNum();
                                        }
                                        else
                                        {
                                            System.out.println("antenna number error");
                                            continue;
                                        }

                                        System.out.println("Setting another antenna");
                                        System.out.println("1.Yes");
                                        System.out.println("2.No");
                                        readKey = sc.next();
                                        if (readKey.equals("1"))
                                        {
                                            continue;
                                        }
                                        else if (readKey.equals("2"))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Parameter error ,please reset");
                                            antNum = 0;
                                        }
                                    }

                                    System.out.println("Please input destroyPassword:");
                                    String destroyPassword = sc.next();

                                    System.out.println("Please input sMatchData:");
                                    String sMatchData = sc.next();

                                    System.out.println("Please input matchWordStartIndex:");
                                    int matchWordStartIndex = Integer.valueOf(sc.next());

                                    if (CLReader._Tag6C.Destroy_MatchTID(ConnID, antNum, destroyPassword, sMatchData, matchWordStartIndex) == 0)
                                    {
                                        System.out.println("Success!");
                                    }
                                    else
                                    {
                                        System.out.println("Failure!");
                                    }
                                }
                                else if (readKey.equals("b"))
                                {
                                    break;
                                }
                                else if (readKey.equals("q"))
                                {
                                    CLReader.Stop(ConnID);
                                    CLReader.CloseConn(ConnID);
                                    return;
                                }
                            }
                        }

                        else if (readKey.equals("s"))
                        {
                            CLReader._Config.Stop(ConnID);
                        }
                        else if (readKey.equals("b"))
                        {
                            break;
                        }
                        else if (readKey.equals("s"))
                        {
                            CLReader._Config.Stop(ConnID);
                        }
                        else if (readKey.equals("q"))
                        {
                            CLReader.Stop(ConnID);
                            CLReader.CloseConn(ConnID);
                            return;
                        }
                    }
                }















                else if (readKey.equals("8"))
                {
                    while (true)
                    {
                        System.out.println("Current ConnID : " + ConnID + "\n");
                        System.out.println("Please select operation:");
                        System.out.println("1.Set Break Point Upload");
                        System.out.println("2.Get Break Point Upload");
                        System.out.println("3.Get Break Point Cache");
                        System.out.println("4.Clear Break Point Cache");
                        System.out.println("b.Back to Previous Menu");
                        System.out.println("q.Quit");

                        readKey = sc.next();
                        if (readKey.equals("1"))
                        {
                            System.out.println("Please select Switch");
                            System.out.println("1.true");
                            System.out.println("2.false");

                            Boolean Switch;

                            readKey = sc.next();
                            if (readKey.equals("1"))
                            {
                                Switch = true;
                            }
                            else if (readKey.equals("2"))
                            {
                                Switch = false;
                            }
                            else
                            {
                                System.out.println("Parameter error,please reset");
                                continue;
                            }

                            System.out.println(CLReader._Config.SetBreakPointUpload(ConnID, Switch));
                        }
                        else if (readKey.equals("2"))
                        {
                            System.out.println(CLReader._Config.GetBreakPointUpload(ConnID));
                        }
                        else if (readKey.equals("3"))
                        {
                            System.out.println(CLReader._Config.GetBreakPointCacheTag(ConnID));
                        }
                        else if (readKey.equals("4"))
                        {
                            System.out.println(CLReader._Config.ClearBreakPointCache(ConnID));
                        }
                        else if (readKey.equals("b"))
                        {
                            break;
                        }
                        else if (readKey.equals("q"))
                        {
                            CLReader.Stop(ConnID);
                            CLReader.CloseConn(ConnID);
                            return;
                        }
                    }
                }






                else if (readKey.equals("q"))
                {
                    CLReader.Stop(ConnID);
                    CLReader.CloseConn(ConnID);
                    return;
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
