package com.aven.qqdemo;

/**
 * 这个实体类是用来保存从接口中解析的数据，需要得到JSON数据中的imageUrl和name两项数据
 * @author user
 *
 */
public class Beauty {
	private String imageUrl;
	private String name;
	public Beauty() {
		super();
	}
	
	public Beauty(String imageUrl, String name) {
		this.imageUrl = imageUrl;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "Beauty [name=" + name + ", imageUrl=" + imageUrl + "]";
	}
	
}