package com.xymtop;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.awt.event.MouseAdapter;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class Pet {
    JFrame frame;
    JLabel pet;
    JPopupMenu popup;
    MsgUi msg;
    static Point mouse = new Point();
    static int WinH = 300;
    static int WinW = 200;
    static int SleepTime = 100;
    static int ThePet = 1;
    static int PetCount = 1;
    static long RunTime = 0;
    static int PetFood = 0;

    private String PetName;
    // falg
    boolean flag = true;
    // 人物是否活动
    boolean active = false;
    // 消息窗口是否打开
    boolean msgui = true;
    // 窗口是否隐藏
    boolean ishide = false;

    public Pet(String name) {
        frame = new JFrame("小白");
        PetName = name;
        if (!name.equals("主程序")) {
            Pet.PetCount++;
        }
        msg = new MsgUi();
        popup = new JPopupMenu();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 框体透明
        frame.setUndecorated(true); // 取消窗口标题栏
        frame.setBackground(new Color(0, 0, 0, 0));// 背景透明
        // 设置位置并显示在最前端
        frame.setBounds(MyWindow.GetWindowW() - Pet.WinW, MyWindow.GetWindowH() - Pet.WinH - 100, Pet.WinW, Pet.WinH);
        frame.setAlwaysOnTop(true);
        // 设置取消窗体任务栏图标
        frame.setType(JFrame.Type.UTILITY);

        pet = new JLabel(new ImageIcon("1.png"));

        frame.add(pet);

        // Walking();
        ChangeImgThread();
        pet.setBounds(0, 0, 200, 200);

        // 拖拽
        new Thread() {
            public void run() {
                pet.setTransferHandler(new TransferHandler() {
                    @Override
                    public boolean importData(JComponent comp, Transferable t) {
                        try {
                            Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                            String filepath = o.toString();
                            if (filepath.startsWith("[")) {
                                filepath = filepath.substring(1);
                            }
                            if (filepath.endsWith("]")) {
                                filepath = filepath.substring(0, filepath.length() - 1);
                            }
                            String[] filename = filepath.split(",");
                            for (String url : filename) {
                                MyFile.moveFileToRecycle(url);
                            }
                            Music.PlayMsg("感谢投喂");
                            Pet.PetFood++;

                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                        for (int i = 0; i < flavors.length; i++) {
                            if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        }.start();

        pet.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {

                    // 鼠标左键单击
                    if (e.getButton() == 1) {
                        Walking();
                    }

                    // 按鼠标中间
                    if (e.getButton() == 2) {
                        WalkDrop();
                    }

                    // 右键
                    if (e.getButton() == 3) {
                        JPopupMenu popup = new JPopupMenu();

                        JMenuItem wall = new JMenuItem("更换专属壁纸");
                        JMenuItem change = new JMenuItem("切换人物");
                        JMenuItem chat = new JMenuItem("和我聊天");
                        JMenuItem help = new JMenuItem("帮助");
                        JMenuItem about = new JMenuItem("关于");
                        JMenuItem hide = new JMenuItem("隐藏");
                        JMenuItem multiboxing = new JMenuItem("多开");
                        JMenuItem exit = new JMenuItem("退出");

                        // 菜单点击事件
                        chat.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                // reg add "hkcu\control panel\desktop" /v wallpaper /d "新壁纸地址(绝对地址）" /f
                                if (msgui) {
                                    msg.SetView(true);
                                } else {
                                    msg = new MsgUi();
                                }
                            }

                        });

                        change.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                // reg add "hkcu\control panel\desktop" /v wallpaper /d "新壁纸地址(绝对地址）" /f
                                File file = new File("res//pet");
                                int num = file.list().length;
                                Pet.ThePet = (int) (Math.random() * num);
                                System.out.println("宠物类型" + Pet.ThePet);
                            }

                        });
                        wall.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                // reg add "hkcu\control panel\desktop" /v wallpaper /d "新壁纸地址(绝对地址）" /f
                                WallPaper.ChangeOne();
                            }

                        });

                        help.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                Util.OpenUrl("https://xymtop.gitee.io/xiaobai/#/");
                            }

                        });

                        about.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                Util.OpenUrl("https://gitee.com/xymtop/xiaobai");
                            }

                        });

                        hide.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                frame.hide();
                                ishide = true;

                            }

                        });

                        multiboxing.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                new Pet("小白" + Pet.PetCount + "号");
                            }

                        });
                        exit.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                msg.dispose();
                                if (PetName.equals("主程序")) {
                                    System.exit(0);
                                } else {
                                    Pet.PetCount--;
                                    frame.dispose();
                                }

                            }

                        });

                        popup.add(new JMenuItem("您好，我是" + PetName + ""));
                        popup.add(change);
                        popup.add(chat);
                        popup.add(wall);
                        popup.add(help);
                        popup.add(about);
                        popup.add(hide);
                        popup.add(multiboxing);
                        popup.add(exit);

                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }

                } else if (e.getClickCount() == 2) {
                    WalkDrop();
                } else {
                    Grabed();
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                mouse.x = e.getX();
                mouse.y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

        });

        // 人物鼠标事件
        pet.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                new Thread() {
                    public void run() {
                        if (!active) {
                            if (!msgui) {
                                new MsgUi();
                                msgui = true;
                            }
                        } else {
                            // System.out.println(e.getX() + " " + e.getY());
                        }

                    }
                }.start();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                new Thread() {
                    public void run() {
                        Point p = frame.getLocation();
                        int x = e.getX() + p.x - mouse.x;
                        int y = e.getY() + p.y - mouse.y;
                        frame.setLocation(x, y);
                    }
                }.start();

            }

        });

        if (PetName.equals("主程序")) {
            new Thread() {
                public void run() {
                    // 打开诗句展示
                    new Poem();
                }
            }.start();

            // 打开托盘
            Opentray();
            // 打开调皮模式
            Operate();
            // 打开远程
            Util.RemoteOperate();
        }

        frame.setVisible(true);

    }

    // 调皮模式
    public void Operate() {
        new Thread() {
            public void run() {
                while (true) {
                    RandOprate();
                    int min = (int) (Math.random() * 10);
                    // System.out.println("时间间隔" + String.valueOf(min));
                    try {
                        sleep(1000 * 60 * min);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    // 改变人物的动作
    public void ChangeImgThread() {
        new Thread() {
            public void run() {
                while (true) {
                    int i = (int) (Math.random() * 5);
                    System.out.println(i);
                    if (i == 0) {
                        Default();
                    } else if (i == 1) {
                        Default();
                    } else if (i == 2) {
                        ClickOne();
                    } else if (i == 3) {
                        WalkDrop();
                    } else if (i == 4) {
                        Grabed();
                    } else {
                        Walking();
                    }

                }

            }

        }.start();
    }

    // 更新渲染图片
    private void ChangeImg(String img) {
        pet.setIcon(new ImageIcon(img));
        // System.out.println(img);
        frame.validate();
        frame.repaint();
    }

    /*
     * 2好默认人物状态参数
     * c 静止
     * a 默认
     * b 转枪
     * e 前进
     * d 向上
     * 
     */
    // 向前行走
    private void Walking() {

        for (int i = 0; i <= MyFile.GetNum("res/pet/" + Pet.ThePet + "/e") - 1; i++) {
            ChangeImg("res/pet/" + String.valueOf(Pet.ThePet) + "/e/e (" + String.valueOf(i) + ")" + ".png");

            if (frame.getLocation().x + 5 >= MyWindow.GetWindowW()) {
                frame.setBounds(frame.getLocation().x + 5, frame.getLocation().y, Pet.WinW, Pet.WinH);
                frame.setBounds(0 + 5, frame.getLocation().y, Pet.WinW, Pet.WinH);
            } else {
                frame.setBounds(frame.getLocation().x + 5, frame.getLocation().y, Pet.WinW, Pet.WinH);
            }
            try {
                Thread.sleep(Pet.SleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // 向上跳
    private void WalkDrop() {

        for (int i = 0; i <= MyFile.GetNum("res/pet/" + Pet.ThePet + "/d") - 1; i++) {
            ChangeImg("res/pet/" + String.valueOf(Pet.ThePet) + "/d/d (" + String.valueOf(i) + ")" + ".png");
            // frame.setBounds(frame.getLocation().x, frame.getLocation().y - 5, Pet.WinW,
            // Pet.WinH);
            if (frame.getLocation().y - 5 <= 0) {
                frame.setBounds(frame.getLocation().x, frame.getLocation().y - 5, Pet.WinW, Pet.WinH);
                frame.setBounds(frame.getLocation().x, MyWindow.GetWindowH() - 5, Pet.WinW, Pet.WinH);
            } else {
                frame.setBounds(frame.getLocation().x, frame.getLocation().y - 5, Pet.WinW, Pet.WinH);
            }
            try {
                Thread.sleep(Pet.SleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 静止状态
    private void ClickOne() {

        for (int i = 0; i <= MyFile.GetNum("res/pet/" + Pet.ThePet + "/c") - 1; i++) {
            ChangeImg("res/pet/" + String.valueOf(Pet.ThePet) + "/c/c (" + String.valueOf(i) + ")" + ".png");
            // frame.setBounds(frame.getLocation().x, frame.getLocation().y - 5, Pet.WinW,
            // Pet.WinH);
            try {
                Thread.sleep(Pet.SleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // 默认状态
    private void Default() {

        for (int i = 0; i <= MyFile.GetNum("res/pet/" + Pet.ThePet + "/a") - 1; i++) {
            ChangeImg("res/pet/" + String.valueOf(Pet.ThePet) + "/a/a (" + String.valueOf(i) + ")" + ".png");
            // frame.setBounds(frame.getLocation().x, frame.getLocation().y - 5, Pet.WinW,
            // Pet.WinH);
            try {
                Thread.sleep(Pet.SleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // 不知道叫啥状态
    private void Grabed() {

        for (int i = 0; i <= MyFile.GetNum("res/pet/" + Pet.ThePet + "/b") - 1; i++) {
            ChangeImg("res/pet/" + String.valueOf(Pet.ThePet) + "/b/b (" + String.valueOf(i) + ")" + ".png");
            // frame.setBounds(frame.getLocation().x, frame.getLocation().y - 5, Pet.WinW,
            // Pet.WinH);
            try {
                Thread.sleep(Pet.SleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 系统托盘
    private void Opentray() {
        // 判断是否支持系统托盘
        if (SystemTray.isSupported()) {
            // 获取图片所在的URL,请把图片放在同一个包下
            // URL url = this.getClass().getResource("res/static/mylogo.png");
            // 实例化图像对象
            ImageIcon icon = new ImageIcon("res/static/mylogo.jpg");
            // 获得Image对象
            Image image = icon.getImage();
            // 创建托盘图标
            TrayIcon trayIcon = new TrayIcon(image);
            // 为托盘添加鼠标适配器
            trayIcon.addMouseListener(new MouseAdapter() {
                // 鼠标事件
                public void mouseClicked(MouseEvent e) {
                    // 判断是否双击了鼠标
                    if (e.getClickCount() == 2) {
                        if (ishide) {
                            frame.setVisible(true);
                        } else {
                            frame.setLocation(0, 0);
                        }

                    }
                }
            });

            new Thread() {
                public void run() {
                    Pet.RunTime = new Date().getTime();
                    while (true) {
                        // 添加工具提示文本
                        trayIcon.setToolTip(
                                "小白\r\n状态：正在运行\r\n程序标识:" + PetName + "\r\n" + "当前小白数量" + Pet.PetCount + "\r\n"
                                        + "当前人物编号: " + Pet.ThePet + "号\r\n" + "当前窗口显示状态:" + !ishide + "\r\n"
                                        + "本次已运行时长:" + String.valueOf((new Date().getTime() - Pet.RunTime) / 1000)
                                        + "秒\r\n" + "本次投喂" + Pet.PetFood + "个文件");
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }
            }.start();
            // 添加工具提示文本
            trayIcon.setToolTip("小白\r\n状态：正在运行\r\n程序标识:" + PetName + "\r\n");
            // 创建弹出菜单
            PopupMenu popupMenu = new PopupMenu();
            popupMenu.add(new MenuItem("投喂站"));
            // popupMenu.add(new MenuItem("关于"));
            popupMenu.add(new MenuItem("退出"));

            // 监听菜单点击事件
            popupMenu.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    String cmd = e.getActionCommand();
                    if (cmd == "退出") {
                        if (PetName.equals("主程序")) {
                            System.exit(0);
                        } else {
                            frame.dispose();
                        }

                    } else if (cmd == "投喂站") {
                        try {
                            Desktop.getDesktop().open(new File(System.getProperty("user.dir") +
                                    "\\recycle"));
                            System.out.println("投喂站");
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }

            });

            popupMenu.addSeparator();

            // 为托盘图标加弹出菜弹
            trayIcon.setPopupMenu(popupMenu);
            // 获得系统托盘对象
            SystemTray systemTray = SystemTray.getSystemTray();
            try {
                // 为系统托盘加托盘图标
                systemTray.add(trayIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "不支持桌面托盘");
        }
    }

    // 多开
    private void CopyPet(int num) {
        for (int i = 0; i < num; i++) {
            new Pet("小白" + Pet.PetCount + "号");
        }
    }

    // 人物随机操作
    private void RandOprate() {
        // System.out.println("我来调皮咯");
        int index = (int) (Math.random() * 9);
        // System.out.println("调皮模式: " + index);
        Pop.CreatePop("调皮模式", "调皮模式" + Integer.valueOf(index), 1000 * 3, 100, 200);
        if (index == 0) {
            // 复制宠物
            CopyPet(1);
        } else if (index == 1) {
            // 打开链接
            Util.OpenUrl("https://xymtop.com");
        } else if (index == 2) {
            // 打开文件
            String url = MyFile.GetDesktopRandUrl();
            try {
                // System.out.println(url);
                Command.RunCmd("start " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (index == 3) {
            // 说话
            try {
                Music.PlayMsg("主人你在干嘛呢");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (index == 4) {
            // 更换壁纸
            WallPaper.ChangeOne();
        } else if (index == 5) {
            try {
                if (!ishide) {
                    Music.PlayMsg("我先去休息啦");
                    frame.setVisible(false);
                    ishide = true;
                } else {
                    Music.PlayMsg("主人我来啦");
                    frame.setVisible(true);
                    ishide = false;
                }

            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (index == 6) {
            String str = Api.GetText();
            try {
                Music.PlayMsg(str);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else if (index == 7) {
            if (Pet.PetFood > 10) {
                try {
                    Music.PlayMsg("今天又是饱饱的一天哦,开心");
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    Music.PlayMsg("主人我好饿呀");
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Music.PlayMsg("我爱你我的宝");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
