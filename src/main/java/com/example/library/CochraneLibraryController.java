package com.example.library;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CochraneLibraryController {
	
	static String topicsUrl = "https://www.cochranelibrary.com/cdsr/reviews/topics";        // To get all the information 
	static String TopicsReviews = "C:\\Users\\16085\\Desktop\\ResultOutPut\\TopicsReviews.html"; // To Store that  topics into topics.html 
	static String topicsFileName  = "C:\\Users\\16085\\Desktop\\ResultOutPut\\Allergy.html"; // To store the specefic information from the url
	static String finalOutPutFileName ="C:\\Users\\16085\\Desktop\\ResultOutPut\\cochrane_reviews.txt"; //final url according to the requirement
	
	
	public static void main(String[] args) {
		viewAllReviewTopics();
	}

	
	//@RequestMapping(value="/reviews-topics", method = RequestMethod.GET)
	public static  void  viewAllReviewTopics() {
		List<String> textList = new ArrayList<String>();
		URL url;
		try {
			url = new URL(topicsUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Language", "en-US"); 
	        con.setRequestProperty("User-Agent",
	                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");

	        con.setUseCaches(false);
	        con.setDoInput(true);
	        con.setDoOutput(true);

			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(TopicsReviews)));
			 
			 //write contents of StringBuffer to a file
			 bwr.write(content.toString());
			 
			 //flush the stream
			 bwr.flush();
			 
			 //close the stream
			 bwr.close();
			 
			 System.out.println("Content of StringBuffer written to File.");
			 
			 
			
			 Document document = Jsoup.parse(new File(TopicsReviews),"utf-8"); 
			 Elements attr = document.select("li[class=browse-by-list-item]");
			 // Elements links = document.select("a[href]"); 
			 for (Element link : attr) {
				  //Node node = link.childNodes().get(1); 
				 List<Node> childNodes =link.childNodes(); 
				 String linkStr = ""; 
				 for(Node hyperLik : childNodes) {
				  linkStr = hyperLik.attr("href"); 
				 } //String attr2 = node.attr("a");
				  System.out.println("link : " + linkStr);
				  System.out.println("text : " +link.text());
				  textList.add(link.text()); 
				 } 
			// }

			in.close();
		} catch (IOException e){
		    e.printStackTrace();
		}
		try{
			String targetURL = "https://www.cochranelibrary.com/en/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_scolarissearchresultsportlet_WAR_scolarissearchresults_displayText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=13&_scolarissearchresultsportlet_WAR_scolarissearchresults_orderBy=displayDate-true&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1506030924307755598196034641807&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics";
			url = new URL(targetURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();

	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", 
	                   "application/x-www-form-urlencoded");
	        connection.setRequestProperty("Content-Language", "en-US"); 
	        connection.setRequestProperty("User-Agent",
	                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");

	        connection.setUseCaches(false);
	        connection.setDoInput(true);
	        connection.setDoOutput(true);

			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(topicsFileName)));
			 
			 //write contents of StringBuffer to a file
			 bwr.write(content.toString());
			 
			 //flush the stream
			 bwr.flush();
			 
			 //close the stream
			 bwr.close();
			 
			 System.out.println("Content of StringBuffer written to File.");
			
			in.close();


			
			Document topicsDdocument = Jsoup.parse(new File(topicsFileName), "utf-8");
			Elements elems = topicsDdocument.select("div[class=search-results-item-body]");
			
			StringBuffer buff = new StringBuffer();
			for(int i=0;i<elems.size();i++) {
				
				 //elems.get(i).attr("a")
				
				Element resultTitleEelement = elems.get(i).select("h3[class=result-title]").get(0);
				String hrefVal = resultTitleEelement.select("a").attr("href");
				
				//System.out.println("hrefVal===> "+hrefVal);
				
				buff.append(hrefVal);
				buff.append("|");
				
				
				String resultTitle = resultTitleEelement.text();
				//System.out.println("resultTitle==> "+resultTitle);
				
				buff.append(resultTitle);
				buff.append("|");
			}
			
			BufferedWriter finalOutPutbw = new BufferedWriter(new FileWriter(new File(finalOutPutFileName)));
			 
			 //write contents of StringBuffer to a file
			finalOutPutbw.write(buff.toString());
			 
			 //flush the stream
			finalOutPutbw.flush();
			 
			 //close the stream
			finalOutPutbw.close();
			 
			 System.out.println("Content of written to File.");

		} catch (IOException e) 		{
		    e.printStackTrace();
		}

			
		 

	
	//model.put("textList",textList);
		 
		
		
		
		// return "reviewsTopics";

		
			
			

	}

}
