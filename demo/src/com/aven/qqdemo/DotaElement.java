package com.aven.qqdemo;

/**
 * ���DotaElement�����ڵ��ĸ�Fragment�����ÿһ�������
 * ���ĸ�Fragment(���༶ListView��ʵ��)��ʵ�ֵ������������ֶεľ������
 * �����Ƿ����ӽڵ�͸��ڵ������һЩ���ݰ���������һЩ������
 * ��Ϊ���ListView��չ���������Ǹ���level�����㣬��remove��֪������һ��item��level�Ǳȵ����item��level(���ڵ���)
 * expanded�������ж��Ƿ���չ��״̬
 * @author user
 *
 */
public class DotaElement {
	private String id;//��ǰ�ڵ��id
	private String outlineTitle ;//�ڵ�������ʾ����Ϣ
	private boolean mhasParent; //�Ƿ��и��ڵ�
	private boolean mhasChild ;//�Ƿ����ӽڵ�
	private String parent;//���ڵ��id
	private int level;//��ǰ�ڵ����ڵĲ��
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

	//������൱��getExpanded()����Ϊ����������жϵģ�ֱ�ӵ���listview.isExpanded()
	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}


	
	
	
	
	
	
	

}
