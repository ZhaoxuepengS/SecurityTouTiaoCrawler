package cn.uniview.securityCrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	public static StringBuffer sb1 = new StringBuffer("...............开源组件相关漏洞：...................\r\n\r\n");
	public static StringBuffer sb2 = new StringBuffer("...............开源组件无关漏洞：...................\r\n\r\n");
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		log.info("开始抓取头条今日漏洞信息");
		String content1 = traceToutiao();
		log.info("开始抓取CVE今日漏洞信息");
		String content2 = traceCVE();
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\CVECrawler\\test.txt")));
		bw.write(sb1.toString());
		bw.write(sb2.toString());
//		bw.write("头条跟踪完整信息。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
//		bw.write(content1);
//		bw.newLine();
//		bw.write("CVE跟踪完整信息。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
//		bw.write(content2);
		bw.flush();
		bw.close();
		String content = sb1.toString()+"\r\n"+sb2.toString();
		sendMails mail = new sendMails();
		mail.sendMail("495149700@qq.com","今日开源组件CVE更新漏洞及头条漏洞",content);
//		String url = "http://cve.mitre.org/cgi-bin/cvename.cgi?name=2017-17096";
//		Document doc = Jsoup.connect(url).ignoreHttpErrors(true).timeout(100000).get();
//		Elements s = doc.select("#GeneratedTable > table > tbody > tr:nth-child(4) > td");
//		System.out.println(s.text());
	}
	private static String traceCVE() throws IOException{
		StringBuffer sb = new StringBuffer();
		readOSExcel readCom = new readOSExcel();
		List<Components> list = readCom.readOSComponentExcel("开源组件使用版本跟踪列表.xlsx");
		sb1.append("CVE相关组件漏洞信息：\r\n");
		sb1.append("\r\n");
		sb2.append("CVE无关组件漏洞信息：\r\n");
		sb2.append("\r\n");
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
					sb1.append("\r\n");
					sb1.append("漏洞相关组件："+com.getDepartment()+":"+comName);
					sb1.append("\r\n");
					sb1.append(cveNo);
					sb1.append("\r\n");
					sb1.append(description);
					sb1.append("\r\n");
					iscontain = true;
				}
			}
			if(!iscontain){
				sb2.append("\r\n");
				sb2.append(cveNo);
				sb2.append("\r\n");
				sb2.append(description);
				sb2.append("\r\n");
			}
			sb.append("\r\n");
			sb.append(cveNo);
			sb.append("\r\n");
			sb.append(description);
			sb.append("\r\n");
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
		sb1.append("头条相关组件漏洞信息：\r\n");
		sb1.append("\r\n");
		sb2.append("头条无关组件漏洞信息：\r\n");
		sb2.append("\r\n");
		while(it.hasNext()){
			articles art = it.next();
			Iterator<Components> comit = list.iterator();
			boolean iscontain = false;
			while(comit.hasNext()){
				Components comp = comit.next();
				if(art.getTitle().contains(comp.getcomponentName())){
					sb1.append("\r\n");
					sb1.append(comp.getcomponentName());
					sb1.append("\r\n");
					sb1.append(art.getTitle());
					sb1.append("\r\n");
					sb1.append(art.gethref());
					sb1.append("\r\n");
					sb1.append(art.getarticle());
					sb1.append("\r\n");
					iscontain = true;
				}
			}
			if(!iscontain){
				sb2.append("\r\n");
				sb2.append(art.getTitle());
				sb2.append("\r\n");
				sb2.append(art.gethref());
				sb2.append("\r\n");
				sb2.append(art.getarticle());
				sb2.append("\r\n");
			}
			contentbf.append(art.getTitle());
			contentbf.append("\r\n");
			contentbf.append(art.gethref());
			contentbf.append("\r\n");
			contentbf.append(art.getarticle());
			contentbf.append("\r\n");
			contentbf.append("\r\n");
			contentbf.append("\r\n");
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