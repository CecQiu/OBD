package com.hgsoft.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class FileUtil {
	private final Log logger = LogFactory.getLog(FileUtil.class);

	public static boolean fileTransfer(File file,String saveFilePath,String fileName){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			File saveFilePathFile = new File(saveFilePath);
			if(!saveFilePathFile.exists())
				saveFilePathFile.mkdirs();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(new File(saveFilePath + "\\" + fileName)));
			byte[] buffer = new byte[1024];
            int length  = 0 ;
            while((length = bis.read(buffer))>0){
                bos.write(buffer, 0, length);
            }
            saveFilePathFile = null;
            buffer = null;
            return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean fileTransfer(String sourcePath,String targetPath,String fileName){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			File sourcePathFile = new File(sourcePath + "/" + fileName);
			if(!sourcePathFile.exists())
				return false;
			File targetPathFile = new File(targetPath);
			if(!targetPathFile.exists())
				targetPathFile.mkdirs();
			bis = new BufferedInputStream(new FileInputStream(sourcePathFile));
			bos = new BufferedOutputStream(new FileOutputStream(new File(targetPath + "//" + fileName)));
			byte[] buffer = new byte[1024];
            int length  = 0 ;
            while((length = bis.read(buffer))>0){
                bos.write(buffer, 0, length);
            }
            sourcePathFile = null;
            targetPathFile = null;
            buffer = null;
            return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getFileContent(File file){
		BufferedReader br = null;
		String content = "";
		try {
			br = new BufferedReader(new FileReader(file));
			String message = null;
			if((message = br.readLine()) != null)
				content += message + "\n";
		}catch (IOException ie) {
			content = null;
		}finally{
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return content;
	}
	
	public static void ImageOutput(HttpServletResponse response,String imagePath,String imageFormat){
		File file = new File(imagePath);
		if(file.isFile() && file.exists()){
			ServletOutputStream outputStream = null;
			try {
				outputStream = response.getOutputStream();
				ImageIO.write(ImageIO.read(file),imageFormat, outputStream);	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(outputStream != null){
					try {
						outputStream.flush();
						outputStream.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static byte[] toByteArray(File file) {
		byte[] rtn=new byte[1024*5];
		try{
			BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			int c=bis.read();
			while(c!=-1) {
				baos.write(c);
				c=bis.read();
			}
			bis.close();
			
			rtn=baos.toByteArray();
			baos.flush();
			baos.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

	/**
	 * 获取文件内容
	 * @param file
	 * @return
	 */
	public static String getFileMsg(File file){
//		 File file = new File("C://Users/Administrator/Desktop/linshi/Demo.hex");
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer("");
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
            	sb.append(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
	}
	/**
	 * 切割文件
	 * @param filePath 源文件路径 
	 * @param outputpath 切割文件的根目录
	 * @throws IOException
	 */
	public static void splitToSmallFiles(String filePath,String outputPath) throws IOException{ 
        //文件计数器，用于产生文件名  
        int filepointer=0;  
        //定义单个文件的最大长度  
        int MAX_SIZE=800;  
        //创建文件输出流  
        BufferedWriter writer=null;  
        //创建文件输入流  
        File file = new File(filePath);
        BufferedReader reader=new BufferedReader(new FileReader(file));  
        //建立字符串缓冲区，存储大文件中读取的数据  
        StringBuffer buffer=new StringBuffer();  
          
        String line=null;
        while((line=reader.readLine())!=null){  
            buffer.append(line).append("\r\n"); 
//            System.out.println(buffer.toString().length());
            if(buffer.toString().length()>=MAX_SIZE){
            	File ff = new File(outputPath+"//"+file.getName().substring(0,file.getName().lastIndexOf("."))+"_"+filepointer+".hex");
            	if(!ff.exists()){
            		ff.createNewFile();
            	}
                writer=new BufferedWriter(new FileWriter(ff));  
                writer.write(buffer.toString());  
                writer.close();  
                filepointer++;  
                buffer.delete(0, buffer.toString().length());  
            }  
        } 
        //将最后一个文件写入
        File ff = new File(outputPath+"//"+file.getName().substring(0,file.getName().lastIndexOf("."))+"_"+filepointer+".hex");
    	if(!ff.exists()){
    		ff.createNewFile();
    	}
        writer=new BufferedWriter(new FileWriter(ff));  
        writer.write(buffer.toString());  
        reader.close();
        writer.close();  
	}
	/**
	 * 文件切割
	 * @param fileName 源文件
	 * @param targetDir 分割后小文件所在的文件夹 
	 * @return
	 * @throws IOException
	 */
	public static void fileCut(String filePath,String targetDir) throws IOException{
//		String fileName=null;//源文件  
	    long unitSize=800;//每个小文件的大小800个byte  
//	    String targetDir=null;//分割后小文件所在的文件夹 
	    File file=new File(filePath);  
        long size=file.length();//总字节数  
        int count=0;//小文件数  
        long pos=0;//当前位置  
        long last=0;//剩余字节数  
        DataInputStream dis=new DataInputStream(new BufferedInputStream(new FileInputStream(file), (int )unitSize));  
        byte[] databuf=new byte[(int)unitSize];  
        while(pos<size)  
        {  
            count ++;  
            last=size-pos;  
            if(last<unitSize){
            	databuf=new byte[(int)last]; 
            }
            dis.read(databuf);  
//            System.out.println("count="+count+";pos="+pos+";databuf.length="+databuf.length);  
            pos=pos+databuf.length;  
            //写小文件  
            try {  
                RandomAccessFile raFile=new RandomAccessFile(targetDir+"//"+file.getName().substring(0,file.getName().lastIndexOf("."))+"_"+count+".bin", "rw");  
                raFile.write(databuf);  
                raFile.close();  
            } catch (Exception e) {  
                throw e;  
            }  
        }  
	}
	/**
	 * 读取文件
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] fileRead(String path) throws IOException{
		File file = new File(path);
		InputStream in = new FileInputStream(file);
		byte[] buf = new byte[(int) file.length()];//一次读取文件长度
		in.read(buf);
		in.close();
		return buf;
	}
	/**
	 * 读取指定文件位置的byte信息
	 * @param file 源文件
	 * @param num 开始序号，开始下标是 unitSize*num
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileByte(File file,int num) throws IOException{
		RandomAccessFile raf=new RandomAccessFile(file, "r");
		long unitSize=10;//每个小文件的大小  
        long size=file.length();//总字节数  
        long last=0;//剩余字节数  
        byte[] databuf=new byte[(int)unitSize];
        last=size-unitSize*num;
        if(last<unitSize){
        	if(last<0){
        		return null;
        	}
        	databuf=new byte[(int)last]; 
        }
        //指针移到指定位置
        raf.seek(num*unitSize);
        raf.read(databuf);
        return databuf;
	}
	/**
	 * 截取byte数组
	 * @param bs 源文件的byte数组
	 * @param num 截取的序号
	 * @return
	 * @throws IOException
	 */
	public static byte[] cutByte(byte[] bs,int num) throws Exception{
		int unitSize=800;//每个小文件的大小  
        int size=bs.length;//总字节数  
        int last=0;//剩余字节数  
        last=size-unitSize*num;
        byte[] msgByte;
		if(last<unitSize){
			if(last<0){
				return null;
			}
			msgByte=Arrays.copyOfRange(bs, num*unitSize, size);
		}else{
			msgByte=Arrays.copyOfRange(bs, num*unitSize, (num+1)*unitSize);
		}
		return msgByte;
	}
	
}
