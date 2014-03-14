package com.example.myapp_xlistview;
/**
 * 资讯详情item实体类
 * @author wangxin
 *
 */
public class NewsContentItem extends BaseContentItem{
	
	private String 	title;
	
	public NewsContentItem(String title, String thumbnail_url, int id,
			String short_content, // Item简介
			String detail_url,// 详情url
			long time) {
		this.title = title;
		this.thumbnail_url = thumbnail_url;
		this.id = id;
		this.short_content = short_content;
		this.detail_url = detail_url;
		this.time = time;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	private String	thumbnail_url;
	
	public String getThumbnail_url() {
		return thumbnail_url;
	}
	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}
	
}
