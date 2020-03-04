package com.mhimine.jdk.coordapp.FileManage;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.Coord.Point;
import com.mhimine.jdk.coordapp.Coord.TransParaSeven;
import com.mhimine.jdk.coordapp.Coord.CoordTransform;
import com.mhimine.jdk.coordapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static final String CoordFilePath = "根目录/Android/data/com.mhimine.jdk.coordapp/files/柠条塔坐标转换/坐标文件";
    public static final String ParamFilePath = "根目录/Android/data/com.mhimine.jdk.coordapp/files/柠条塔坐标转换/转换参数";

    public static String getPath(final Context context, final Uri uri) {

        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //读取文件（参数：文件路径，返回String）
    public static String readFile(String filePath) {
        try {
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline;
            StringBuffer sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                sb.append(readline);
                sb.append("\n");
            }
            br.close();
            sb.deleteCharAt(sb.length() - 1);
            return FileUtils.specialUnicode(sb.toString());
        } catch (Exception e) {
            //e.printStackTrace();
            return "";
        }
    }

    public static String getStringByInputStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //输入文件（参数：文件路径、文件写入内容）
    public static void saveFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            /**
             * 为了提高写入的效率，使用了字符流的缓冲区。
             * 创建了一个字符写入流的缓冲区对象，并和指定要被缓冲的流对象相关联。
             */
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));

            //使用缓冲区中的方法将数据写入到缓冲区中。
            bw.write(content);
            //bw.newLine();
            //使用缓冲区中的方法，将数据刷新到目的地文件中去。
            bw.flush();
            //关闭缓冲区,同时关闭了FileWriter流对象
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //文件转换-保存转换文件
    public static boolean saveTargetPoint(String fileName, List<Point> targetPoints) {
        try {
            File file = new File(fileName);
            /**
             * 为了提高写入的效率，使用了字符流的缓冲区。
             * 创建了一个字符写入流的缓冲区对象，并和指定要被缓冲的流对象相关联。
             */
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (int i = 0; i < targetPoints.size(); i++) {
                //使用缓冲区中的方法将数据写入到缓冲区中。
                StringBuffer sb = new StringBuffer();
                sb.append(targetPoints.get(i).getPointName());
                sb.append(",");
                sb.append(targetPoints.get(i).getX());
                sb.append(",");
                sb.append(targetPoints.get(i).getY());
                sb.append(",");
                sb.append(targetPoints.get(i).getZ());
                bw.write(sb.toString());
//                bw.write(targetPoints.get(i).getPointName());
//                bw.write(",");
//                bw.write(String.valueOf(targetPoints.get(i).getX()));
//                bw.write(",");
//                bw.write(String.valueOf(targetPoints.get(i).getY()));
//                bw.write(",");
//                bw.write(String.valueOf(targetPoints.get(i).getZ()));
                if (i < targetPoints.size() - 1) {
                    bw.newLine();
                }
            }
            //使用缓冲区中的方法，将数据刷新到目的地文件中去。
            bw.flush();
            //关闭缓冲区,同时关闭了FileWriter流对象
            bw.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };

    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0)
            return type;
        /* 获取文件的后缀名 */
        String fileType = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (fileType == null || "".equals(fileType))
            return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (fileType.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 保存内容到内部存储器中
     *
     * @param filename 文件名
     * @param context  内容
     */
    public static void SaveInStorage(Context context, String filename,String content) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);//openFileOutput("data", Context.MODE_PRIVATE);
            //File file = new File(context.getFilesDir(), filename);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过文件名获取内容
     *
     * @param filename 文件名
     * @return 文件内容
     */
    public static String getInStorage(Context context, String filename) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = context.openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
                content.append("\n");
            }
            content.deleteCharAt(content.length() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return FileUtils.specialUnicode(content.toString());
        }
    }

    public static String saveParaFile(Context context){
        StringBuffer sb = new StringBuffer();
        if (CoordTransform.SevenParames.size() == 0) {
            return "";
        }
        for (Map.Entry<String, TransParaSeven> entry :  CoordTransform.SevenParames.entrySet()) {
            String coordTransTypeStr = entry.getKey();
            if (coordTransTypeStr.equals("")) {
                continue;
            }
            sb.append(coordTransTypeStr);
            sb.append(";");
            sb.append(entry.getValue().getOffsetX());
            sb.append(";");
            sb.append(entry.getValue().getOffsetY());
            sb.append(";");
            sb.append(entry.getValue().getOffsetZ());
            sb.append(";");
            sb.append(entry.getValue().getRotateX());
            sb.append(";");
            sb.append(entry.getValue().getRotateY());
            sb.append(";");
            sb.append(entry.getValue().getRotateZ());
            sb.append(";");
            sb.append(entry.getValue().getScale());
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
        //saveInStorage(context,savePaht,sb.toString());
    }

    public static Map<String,TransParaSeven> restoreMapFromFile(Context context,String inputStr){
        Map<String,TransParaSeven> SevenParames = new HashMap<>();
        inputStr = FileUtils.specialUnicode(inputStr);
        String[] transParams = inputStr.split("\n");
        for (int i = 0; i < transParams.length; i++) {
            String[] paraLine = transParams[i].split(";");
            if (paraLine.length != 9) {
                Toast.makeText(context.getApplicationContext(),"请检查参数文件格式（第" + i+1 + "行）是否正确!",Toast.LENGTH_SHORT).show();
                SevenParames.clear();
                break;
            }

            Resources res =context.getResources();
            String[] coords = res.getStringArray(R.array.spinnerCoord);
            List<String> coordsList = Arrays.asList(coords);

            if (coordsList.contains(paraLine[0]) && coordsList.contains(paraLine[1])){
                String coordTypeStr = paraLine[0] + ";" + paraLine[1];
                try{
                    double offsetX = Double.parseDouble(paraLine[2]);
                    double offsetY = Double.parseDouble(paraLine[3]);
                    double offsetZ = Double.parseDouble(paraLine[4]);
                    double rotateX = Double.parseDouble(paraLine[5]);
                    double rotateY = Double.parseDouble(paraLine[6]);
                    double rotateZ = Double.parseDouble(paraLine[7]);
                    double scale = Double.parseDouble(paraLine[8]);
                    TransParaSeven transParaSeven = new TransParaSeven(offsetX,offsetY,offsetZ,rotateX,rotateY,rotateZ,scale);
                    SevenParames.put(coordTypeStr,transParaSeven);
                }
                catch (Exception e){
                    SevenParames.clear();
                    Toast.makeText(context.getApplicationContext(),"请检查参数文件格式（第" + i+1 + "行）是否正确!",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(context.getApplicationContext(),"请检查参数文件格式（第" + i+1 + "行）是否正确!",Toast.LENGTH_SHORT).show();
                SevenParames.clear();
                break;
            }

        }
        return SevenParames;
    }

    //从坐标文件List<Point>
    public static List<Point> getPoints(String inputPath,Context context){
        String inputStr = FileUtils.readFile(inputPath);
        String[] inputLines = inputStr.split("\n");
        List<Point> points = new ArrayList<>();
        for(int i = 0; i < inputLines.length; i++){
            if (inputLines[i].equals("")) {
                continue;
            }
            String[] pointStr = inputLines[i].split(",");
            if (pointStr.length != 4) {
                Toast.makeText(context.getApplicationContext(),"请检查待转坐标文本格式（第" + i+1 + "行）是否正确!",Toast.LENGTH_SHORT).show();
                return null;
            }
            String pointName = pointStr[0];
            try{
                double x = Double.parseDouble(pointStr[1]);
                double y = Double.parseDouble(pointStr[2]);
                double z = Double.parseDouble(pointStr[3]);
                points.add(new Point(pointName, x, y, z));
            }
            catch (Exception e){
                Toast.makeText(context.getApplicationContext(),"请检查待转坐标文本格式（第" + i+1 + "行）是否正确!",Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return points;
    }

    /**
     * 去除 字符串收尾的 特殊的Unicode [ "\uFEFF" ]
     * csv 文件可能会带有该编码
     * @param str
     * @return
     */
    public static String specialUnicode(String str){
        if (str.startsWith("\uFEFF")){
            str = str.replace("\uFEFF", "");
        }else if (str.endsWith("\uFEFF")){
            str = str.replace("\uFEFF","");
        }
        return str;
    }

    public static final int FLAG_SUCCESS = 1;//创建成功
    public static final int FLAG_EXISTS = 2;//已存在
    public static final int FLAG_FAILED = 3;//创建失败

    /**
     * 创建 单个 文件
     * @param filePath 待创建的文件路径
     * @return 结果码
     */
    public static int CreateFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
//            Log.e(TAG,"The file [ " + filePath + " ] has already exists");
            return FLAG_EXISTS;
        }
        if (filePath.endsWith(File.separator)) {// 以 路径分隔符 结束，说明是文件夹
//            Log.e(TAG,"The file [ " + filePath + " ] can not be a directory");
            return FLAG_FAILED;
        }

        //判断父目录是否存在
        if (!file.getParentFile().exists()) {
            //父目录不存在 创建父目录
//            Log.d(TAG,"creating parent directory...");
            try {
                if (!file.getParentFile().mkdirs()) {
                    return FLAG_FAILED;
                }
            }catch (Exception e) {
                e.printStackTrace();
                return FLAG_FAILED;
            }

        }

        //创建目标文件
        try {
            if (file.createNewFile()) {//创建文件成功
//                Log.i(TAG, "create file [ " + filePath + " ] success");
                return FLAG_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e(TAG,"create file [ " + filePath + " ] failed");
            return FLAG_FAILED;
        }

        return FLAG_FAILED;
    }

    /**
     * 创建 文件夹
     * @param dirPath 文件夹路径
     * @return 结果码
     */
    public static int createDir (String dirPath) {

        File dir = new File(dirPath);
        //文件夹是否已经存在
        if (dir.exists()) {
//            Log.w(TAG,"The directory [ " + dirPath + " ] has already exists");
            return FLAG_EXISTS;
        }
        if (!dirPath.endsWith(File.separator)) {//不是以 路径分隔符 "/" 结束，则添加路径分隔符 "/"
            dirPath = dirPath + File.separator;
        }
        //创建文件夹
        if (dir.mkdirs()) {
//            Log.d(TAG,"create directory [ "+ dirPath + " ] success");
            return FLAG_SUCCESS;
        }

//        Log.e(TAG,"create directory [ "+ dirPath + " ] failed");
        return FLAG_FAILED;
    }
    //读取指定目录下的所有TXT文件的文件名
    public static List<String> getFileName(File rootFile, List<String> txtNames, String level) {
        //List<String> txtNames = new ArrayList<>();

//        if (files != null) { // 先判断目录是否为空，否则会报空指针  
//            for (File file : files) {
//                if (file.isDirectory()){//检查此路径名的文件是否是一个目录(文件夹) 
//                    getFileName(file.listFiles(), txtNames);
//                }
//                else {
//                    String fileName = file.getName();
//                    if (fileName.endsWith(".txt")) {
//                        String s= fileName.substring(0,fileName.lastIndexOf(".")).toString();
//                        txtNames.add(s);
//                        //str += fileName.substring(0,fileName.lastIndexOf("."))+"\n";
//                    }
//                }
//            }
//        }
        if (rootFile.exists()) {
            File[] allFiles = rootFile.listFiles();
            for (int i = 0; i < allFiles.length; i++) {
                File file = allFiles[i];

                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".txt")) {
                        //String fileNameNoSuffix= fileName.substring(0,fileName.lastIndexOf(".")).toString();
                        if (level == ""){
                            txtNames.add(fileName);
                        }
                        else {
                            txtNames.add(level +  "/" + fileName);
                        }
                    }
                } else  {
                    getFileName(file,txtNames,file.getName());
                }
            }
        }
        return txtNames;
    }

    public static File asFile(InputStream inputStream,File tmp) throws IOException{

        //File tmp = File.createTempFile("manual", ".pdf", new File(tempPath));
        OutputStream os = new FileOutputStream(tmp);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        return tmp;
    }


    public static List<FileInfo> getFileInfos(File rootFile, List<FileInfo> txtNames, String level) {
        if (rootFile.exists()) {
            File[] allFiles = rootFile.listFiles();
            for (int i = 0; i < allFiles.length; i++) {
                File file = allFiles[i];

                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".txt")) {
                        //String fileNameNoSuffix= fileName.substring(0,fileName.lastIndexOf(".")).toString();
                        String fileFullPath = file.getPath();
                        if (level == ""){
                            txtNames.add(new FileInfo(fileName,fileFullPath));
                        }
                        else {
                            txtNames.add(new FileInfo(level +  "/" + fileName, fileFullPath));
                        }
                    }
                } else  {
                    getFileInfos(file,txtNames,file.getName());
                }
            }
        }
        return txtNames;
    }

    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

}
