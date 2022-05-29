package com.tan.labbackend.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FtpUtils {
    private static final String server = "47.103.9.250";
    //private static final String server = "192.168.75.3";
    private static final int port = 21;
    private static final String userName = "hnc";
    private static final String userPassword = "123";
    private static final String ftpPath0 = "files";
    private static FTPClient ftpClient = new FTPClient();
    private static String LOCAL_CHARSET = "GBK";
    private static String SERVER_CHARSET = "ISO-8859-1";
    private final static String localpath = "D:\\download";
    private final static String localpath2 = "D:\\cache";
    private final static String fileSeparator = System.getProperty("file.separator");

    public static boolean upload(MultipartFile uploadFile, Integer cour_id, Integer pro_id, Integer stu_id) {
        boolean result = false;
        try {
//            ftpClient.enterLocalPassiveMode();
            ftpClient.connect(server, port);
            ftpClient.enterLocalActiveMode();
            ftpClient.login(userName, userPassword);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            String ftpPath = ftpPath0 + "/" + cour_id +"/xiangmu" +"/" + pro_id + "/" + stu_id;
            mkDir(ftpPath);// 创建目录
            // 设置上传目录 must
            ftpClient.changeWorkingDirectory("/" + ftpPath);
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            String fileName = new String(uploadFile.getOriginalFilename().getBytes(LOCAL_CHARSET), SERVER_CHARSET);

            FTPFile[] fs = ftpClient.listFiles(fileName);
            if (fs.length == 0) {
                System.out.println("this file not exist ftp");
            } else if (fs.length == 1) {
                System.out.println("this file exist ftp");
                ftpClient.deleteFile(fs[0].getName());
            }
            InputStream is = uploadFile.getInputStream();
            result = ftpClient.storeFile(fileName, is);
            is.close();
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
                System.out.println("上传完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean mkDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            // System.out.println("ftpPath:" + ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.makeDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] downFileByte(String remotePath, String fileName) throws Exception {
        File folder = new File(localpath);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
            System.out.println("创建文件夹");
        } else {
            System.out.println("文件夹已存在");
        }
        ftpClient.connect(server, port);
        ftpClient.login(userName, userPassword);
        ftpClient.enterLocalActiveMode();
        if (remotePath != null && !remotePath.equals("")) {
            ftpClient.changeWorkingDirectory(remotePath);
            System.out.println("file success");
        }
        byte[] return_arraybyte = null;
        if (ftpClient != null) {
            try {
                FTPFile[] files = ftpClient.listFiles();
                for (FTPFile file : files) {
                    String f = new String(file.getName().getBytes("iso-8859-1"), "utf-8");//防止乱码
                    System.out.println(f);
                    System.out.println(f.equals(fileName));
                    if (f.equals(fileName)) {
                        InputStream ins = ftpClient.retrieveFileStream(file.getName());//需使用file.getName获值，若用f会乱码
                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        byte[] buf = new byte[204800];
                        int bufsize = 0;
                        while ((bufsize = ins.read(buf, 0, buf.length)) != -1) {
                            byteOut.write(buf, 0, bufsize);
                        }
                        return_arraybyte = byteOut.toByteArray();

                        File localFile = new File(localpath + fileSeparator + f);
                        OutputStream is = new FileOutputStream(localFile);
                        is.write(return_arraybyte);
                        ftpClient.retrieveFile(f, is);

                        is.close();
                        byteOut.close();
                        ins.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnect();
            }
        }
        return return_arraybyte;
    }

    public static void closeConnect() {
        try {
            ftpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean dataupload(MultipartFile uploadFile, Integer cour_id,Integer pro_id){
        boolean result = false;
        try {
//            ftpClient.enterLocalPassiveMode();
            ftpClient.connect(server, port);
            ftpClient.enterLocalActiveMode();
            ftpClient.login(userName, userPassword);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            String ftpPath = ftpPath0 + "/" + cour_id + "/ziliao/";
            mkDir(ftpPath);// 创建目录
            // 设置上传目录 must
            ftpClient.changeWorkingDirectory("/" + ftpPath);
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            String fileName = new String(uploadFile.getOriginalFilename().getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            FTPFile[] fs = ftpClient.listFiles(fileName);
            if (fs.length == 0) {
                System.out.println("this file not exist ftp");
            } else if (fs.length == 1) {
                System.out.println("this file exist ftp");
                ftpClient.deleteFile(fs[0].getName());
            }
            InputStream is = uploadFile.getInputStream();
            result = ftpClient.storeFile(fileName, is);
            is.close();
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
                System.out.println("上传完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean deleteFileByte(String remotePath, String fileName) throws Exception {
        boolean result = false;
        ftpClient.connect(server, port);
        ftpClient.login(userName, userPassword);
        ftpClient.enterLocalActiveMode();
        if (remotePath != null && !remotePath.equals("")) {
            ftpClient.changeWorkingDirectory(remotePath);
            System.out.println("file success");
        }
        byte[] return_arraybyte = null;
        if (ftpClient != null) {
            try {
                FTPFile[] files = ftpClient.listFiles();
                for (FTPFile file : files) {
                    String f = new String(file.getName().getBytes("iso-8859-1"), "utf-8");//防止乱码
                    System.out.println(f);
                    System.out.println(f.equals(fileName));
                    if (f.equals(fileName)) {
                        ftpClient.dele(remotePath+fileName);
                        result=true;
                        break;
                    }
                }
            } catch (Exception e) {
                result=false;
                e.printStackTrace();
            } finally {
                result=true;
                closeConnect();
            }
        }
        return result;
    }

    //预览
    public static byte[] FileByte(String remotePath, String fileName) throws Exception {
        File folder = new File(localpath2);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
            System.out.println("创建文件夹");
        } else {
            System.out.println("文件夹已存在");
        }
        ftpClient.connect(server, port);
        ftpClient.login(userName, userPassword);
        ftpClient.enterLocalActiveMode();
        if (remotePath != null && !remotePath.equals("")) {
            ftpClient.changeWorkingDirectory(remotePath);
            System.out.println("file success");
        }
        byte[] return_arraybyte = null;
        if (ftpClient != null) {
            try {
                FTPFile[] files = ftpClient.listFiles();
                for (FTPFile file : files) {
                    String f = new String(file.getName().getBytes("iso-8859-1"), "utf-8");//防止乱码
                    System.out.println(f);
                    System.out.println(f.equals(fileName));
                    if (f.equals(fileName)) {
                        InputStream ins = ftpClient.retrieveFileStream(file.getName());//需使用file.getName获值，若用f会乱码
                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        byte[] buf = new byte[204800];
                        int bufsize = 0;
                        while ((bufsize = ins.read(buf, 0, buf.length)) != -1) {
                            byteOut.write(buf, 0, bufsize);
                        }
                        return_arraybyte = byteOut.toByteArray();

                        File localFile = new File(localpath2 + fileSeparator + f);
                        OutputStream is = new FileOutputStream(localFile);
                        is.write(return_arraybyte);
                        ftpClient.retrieveFile(f, is);

                        is.close();
                        byteOut.close();
                        ins.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnect();
            }
        }
        return return_arraybyte;
    }

    public static boolean delfile(String fileName){
        boolean flag = false;
        File file = new File(localpath2);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++){
            if(tempList[i].equals(fileName)){
                temp=new File(localpath2+"\\"+tempList[i]);
                temp.delete();
                flag=true;
            }
        }
        return flag;
    }

}
