package com.data.baidu;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;

/**
 * Created by songxiaomin on 2017/3/2.
 */
public class Seg {
    //分词函数
    public static void segm(File f,File destFile)throws Exception{
        BufferedWriter bw=new BufferedWriter(new FileWriter(destFile));
        try {
            BufferedReader br=new BufferedReader(new FileReader(f));
            String re=null;
            String str="";
            while((re=br.readLine())!=null){
                str+=re;

            }
            StringReader sr=new StringReader(str);
            IKSegmenter ik=new IKSegmenter(sr,true);
            Lexeme le=null;
            StringBuilder sb=new  StringBuilder();

            while((le=ik.next())!=null){
                sb.append(le.getLexemeText()).append(" ");

            }

            bw.write(sb.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            bw.close();
        }


    }

}
