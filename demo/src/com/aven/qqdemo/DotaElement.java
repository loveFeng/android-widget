package com.aven.qqdemo;

/**
 * 这个DotaElement类是在第四个Fragment里面的每一项的数据
 * 第四个Fragment(即多级ListView的实现)的实现得益于这个类的字段的精妙设计
 * 根据是否有子节点和父节点可以让一些数据包含在另外一些数据中
 * 因为这个ListView的展开和收缩是根据level来计算，即remove到知道碰到一个item的level是比点击的item的level(大于等于)
 * expanded是用来判断是否是展开状态
 * @author user
 *
 */
public class DotaElement {
	private String id;//当前节点的id
	private String outlineTitle ;//节点上面显示的信息
	private boolean mhasParent; //是否有父节点
	private boolean mhasChild ;//是否有子节点
	private String parent;//父节点的id
	private int level;//当前节点所在的层次
	private boolean expanded;//private OutlineElement outlineElement;
	
	public DotaElement(String id, String outlineTitle,
			boolean mhasParent, boolean mhasChild, String parent, int level,
			boolean expanded) {
		super();
		this.id = id;
		this.outlineTitle = outlineTitle;
		this.mhasParent = mhasParent;
		this.mhasChild = mhasChild;
		this.parent = parent;
		this.level = level;
		this.expanded = expanded;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOutlineTitle() {
		return outlineTitle;
	}

	public void setOutlineTitle(String outlineTitle) {
		this.outlineTitle = outlineTitle;
	}

	public boolean isMhasParent() {
		return mhasParent;
	}

	public void setMhasParent(boolean mhasParent) {
		this.mhasParent = mhasParent;
	}

	public boolean isMhasChild() {
		return mhasChild;
	}

	public void setMhasChild(boolean mhasChild) {
		this.mhasChild = mhasChild;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	//这个就相当于getExpanded()，因为这个是用来判断的，直接调用listview.isExpanded()
	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}


	
	
	
	
	
	
	

}
