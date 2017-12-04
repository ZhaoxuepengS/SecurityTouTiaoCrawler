package cn.uniview.securityCrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String url = "http://toutiao.secjia.com/";
	
		Document doc = Jsoup.connect(url).ignoreHttpErrors(true).get();
		
		File poFile = new File("D:\\pointer.txt");
		
		BufferedReader bfread = new BufferedReader(new FileReader(poFile));
		String pointString = bfread.readLine();
		FileWriter fw = new FileWriter(poFile,false);
		articles art = null;
		ArrayList<articles> artList = new ArrayList<>();
		
		Elements s = doc.select("div.block_home_post");
		for (Element element : s) {
			String title = element.select("h3.title").text();
			String article = element.select("p.profile").text();
			String href = "http://toutiao.secjia.com/"+element.select("h3.title > a").attr("href");
			art = new articles();
			art.setarticle(article);
			art.setTitle(title);
			art.sethref(href);
			artList.add(art);
		}

		int i=0;
loop:	while(i<11){
			if(i==0){
				String url2 = "http://toutiao.secjia.com/limit/10/offset/0";
				Document doc2 = Jsoup.connect(url2).ignoreHttpErrors(true).get();
				Elements s2 = doc2.select("div.block_home_post");
				for (int j = 0; j < s2.size(); j++) {
					if(j==0){
						fw.write(s2.get(j).select("h3.title").text());
						fw.flush();
						fw.close();
					}
					String title = s2.get(j).select("h3.title").text();
					String article = s2.get(j).select("p.profile").text();
					String href = "http://toutiao.secjia.com/"+s2.get(j).select("h3.title > a").attr("href");
					if(!title.equals(pointString)){
						art = new articles();
						art.setarticle(article);
						art.setTitle(title);
						art.sethref(href);
						artList.add(art);
					}else{
						break loop;
					}
					
				} 
			}else{
				String url2 = "http://toutiao.secjia.com/limit/10/offset/"+i+"0";
				Document doc2 = Jsoup.connect(url2).ignoreHttpErrors(true).get();
				Elements s2 = doc2.select("div.block_home_post");
				for (int j = 0; j < s2.size(); j++) {
					String title = s2.get(j).select("h3.title").text();
					String article = s2.get(j).select("p.profile").text();
					String href = "http://toutiao.secjia.com/"+s2.get(j).select("h3.title > a").attr("href");
					if(!title.equals(pointString)){
						art = new articles();
						art.setarticle(article);
						art.setTitle(title);
						art.sethref(href);
						artList.add(art);
					}else{
						break;
					}
				} 
			}
			i++;
		}
		String content = content(artList);
		fw.close();
		bfread.close();
	}
	public static String content(ArrayList<articles> artList){
		Iterator<articles> it = artList.iterator();
		StringBuffer contentbf = new StringBuffer();
		while(it.hasNext()){
			articles art = it.next();
			contentbf.append("<a href=\""+art.gethref()+"\">"+art.getTitle()+"</a>");
			contentbf.append("\r\n");
			contentbf.append(art.getarticle());
			contentbf.append("\r\n");
		}
		System.out.println(contentbf.toString());
		return contentbf.toString();
	}

}

class articles{
	private String title;
	private String article;
	private String href;
	
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return title;
	}
	
	public void setarticle(String article){
		this.article = article;
	}
	public String getarticle(){
		return article;
	}
	
	public void sethref(String href){
		this.href = href;
	}
	public String gethref(){
		return href;
	}
}