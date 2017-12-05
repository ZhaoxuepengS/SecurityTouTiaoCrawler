package cn.uniview.securityCrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mail.sendMails;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import components.Components;


import CVEs.CVEs;

public class Crawler {
	public static Logger log = Logger.getLogger("Test");
	public static StringBuffer sb1 = new StringBuffer("<h1>...............开源组件相关漏洞：...................<h1><br /><br />");
	public static StringBuffer sb2 = new StringBuffer("<h1>...............开源组件无关漏洞：...................<h1><br /><br />");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		log.info("开始抓取头条今日漏洞信息");
		String content1 = traceToutiao();
		log.info("开始抓取CVE今日漏洞信息");
		String content2 = traceCVE();
		File attament = new File("D:\\Zhaoxuepeng\\test.html");
		BufferedWriter bw = new BufferedWriter(new FileWriter(attament));
		bw.write(sb1.toString());
		bw.write(sb2.toString());
//		bw.write("头条跟踪完整信息。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
//		bw.write(content1);
//		bw.newLine();
//		bw.write("CVE跟踪完整信息。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
//		bw.write(content2);
		bw.flush();
		bw.close();
		String content = sb1.toString()+"<br />"+sb2.toString();
		sendMails mail = new sendMails();
		mail.sendMail("495149700@qq.com","今日开源组件CVE更新漏洞及头条漏洞",content,attament);
		}catch(Exception e){
			log.error(e.getStackTrace());
		}
//		String url = "http://cve.mitre.org/cgi-bin/cvename.cgi?name=2017-17096";
//		Document doc = Jsoup.connect(url).ignoreHttpErrors(true).timeout(100000).get();
//		Elements s = doc.select("#GeneratedTable > table > tbody > tr:nth-child(4) > td");
//		System.out.println(s.text());
	}
	private static String traceCVE() throws IOException{
		StringBuffer sb = new StringBuffer();
		readOSExcel readCom = new readOSExcel();
		List<Components> list = readCom.readOSComponentExcel("D:\\Zhaoxuepeng\\开源组件使用版本跟踪列表.xlsx");
		sb1.append("<h2>CVE相关组件漏洞信息：<h2><br />");
		sb1.append("<br />");
		sb2.append("<h2>CVE无关组件漏洞信息：<h2><br />");
		sb2.append("<br />");
		String url = "https://cassandra.cerias.purdue.edu/CVE_changes/today.html";
		long start = System.currentTimeMillis();  
		Document doc = Jsoup.connect(url).ignoreHttpErrors(true).timeout(50000).get();
		System.out.println("Time is:"+(System.currentTimeMillis()-start) + "ms");  

		log.info("连接CVE每日更新日志网页");
		Elements s = doc.select("a");
		log.info("抓取更新CVE共"+s.size()+"个");
		for (Element element : s) {
			String cveNo = element.text();
			log.info("开始追踪："+cveNo);
			String cveurl = element.attr("href");
			Document doc2 = Jsoup.connect(cveurl).ignoreHttpErrors(true).timeout(500000).get();
			Elements s1 = doc2.select("#GeneratedTable > table > tbody > tr:nth-child(4) > td");
			String description = s1.text();

			Iterator<Components> it = list.iterator();
			String comName = "";
			boolean iscontain = false;
			while(it.hasNext()){
				Components com = it.next();
				comName = com.getcomponentName();
				if(description.contains(comName)){
					sb1.append("<br />");
					sb1.append("<font size=4>漏洞相关组件："+com.getDepartment()+":"+comName+"</font>");
					sb1.append("<br />");
					sb1.append("<font size=4>"+cveNo+"</font>");
					sb1.append("<br />");
					sb1.append("<font size=4>"+description+"</font>");
					sb1.append("<br />");
					iscontain = true;
				}
			}
			if(!iscontain){
				sb2.append("<br />");
				sb2.append("<font size=4>"+cveNo+"</font>");
				sb2.append("<br />");
				sb2.append("<font size=4>"+description+"</font>");
				sb2.append("<br />");
			}
			sb.append("<br />");
			sb.append(cveNo);
			sb.append("<br />");
			sb.append(description);
			sb.append("<br />");
		}
		log.info("CVE更新漏洞抓取写入完毕");
		return sb.toString();
		 
	}
	private static String traceToutiao() throws IOException,
			FileNotFoundException {
		log.info("开始抓取头条漏洞信息：");
		String url = "http://toutiao.secjia.com/";
	
		Document doc = Jsoup.connect(url).ignoreHttpErrors(true).timeout(10000).get();
		log.info("连接头条网页");
		File poFile = new File("D:\\Zhaoxuepeng\\pointer.txt");
		
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
				Document doc2 = Jsoup.connect(url2).ignoreHttpErrors(true).timeout(10000).get();
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
				Document doc2 = Jsoup.connect(url2).ignoreHttpErrors(true).timeout(10000).get();
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
		log.info("抓取头条信息完毕");
		return content;
	}
	public static String content(ArrayList<articles> artList) throws FileNotFoundException, IOException{
		readOSExcel readCom = new readOSExcel();
		List<Components> list = readCom.readOSComponentExcel("开源组件使用版本跟踪列表.xlsx");
		Iterator<articles> it = artList.iterator();
		StringBuffer contentbf = new StringBuffer();
		sb1.append("<h2>头条相关组件漏洞信息：<h2><br />");
		sb1.append("<br />");
		sb2.append("<h2>头条无关组件漏洞信息：<h2><br />");
		sb2.append("<br />");
		while(it.hasNext()){
			articles art = it.next();
			Iterator<Components> comit = list.iterator();
			boolean iscontain = false;
			while(comit.hasNext()){
				Components comp = comit.next();
				if(art.getTitle().contains(comp.getcomponentName())){
					sb1.append("<br />");
					sb1.append("<font size=4>"+comp.getcomponentName()+"</font>");
					sb1.append("<br />");
					sb1.append("<font size=4>"+art.getTitle()+"</font>");
					sb1.append("<br />");
					sb1.append("<font size=4>"+art.gethref()+"</font>");
					sb1.append("<br />");
					sb1.append("<font size=4>"+art.getarticle()+"</font>");
					sb1.append("<br />");
					iscontain = true;
				}
			}
			if(!iscontain){
				sb2.append("<br />");
				sb2.append("<font size=4>"+art.getTitle()+"</font>");
				sb2.append("<br />");
				sb2.append("<font size=4>"+art.gethref()+"</font>");
				sb2.append("<br />");
				sb2.append("<font size=4>"+art.getarticle()+"</font>");
				sb2.append("<br />");
			}
			contentbf.append(art.getTitle());
			contentbf.append("<br />");
			contentbf.append(art.gethref());
			contentbf.append("<br />");
			contentbf.append(art.getarticle());
			contentbf.append("<br />");
			contentbf.append("<br />");
			contentbf.append("<br />");
		}

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