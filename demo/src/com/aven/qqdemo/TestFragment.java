package com.aven.qqdemo;

import java.util.ArrayList;

import com.example.demo_highlights.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * �����
 * @author user
 *
 */
public class TestFragment extends Fragment {
	public static final int GRID_THREAD = 0;
	public static final int LIST_THREAD = 1;
	public static final int GALLERY_THREAD = 5;
    private static final String TAG = "TestFragment";
    private int num;
    private View view;
    private int defaultNum = -1;
    private double defaultWidth = 30;
    private double defaultHeight = 30;
    private Context context;
    private String url = "http://agent1.pconline.com.cn:8941/photolib/iphone_cate_json.jsp?id=25";
    
    private TextView textview_lay3;
    private TextView textview_lay4;
    private GridView gridView;
    private ListView listView;
    private GridAdapter gridAdapter;
    private ListAdapter listAdapter;
    //private GridHandler gridHandler;
    //private ListHandler listHandler;
    private int imageWidth;
    private int imageHeight;
    //private LinearLayout.LayoutParams imageLayoutParams;
    private Handler handler;
    
    private ListView listView_treeview;
    private ArrayList<DotaElement> mPdfOutlinesCount = new ArrayList<DotaElement>();
	private ArrayList<DotaElement> mPdfOutlines = new ArrayList<DotaElement>();
	private TreeViewAdapter treeViewAdapter;
	
	private MyGallery gallery;
	private MyGalleryAdapter galleryAdapter;
	public static ArrayList<Beauty> gallery_data;
    

	/**
	 * �����newInstance(int s)�����н�����MainActivity�д�������ֵ
	 * ���ֵ������onCreateView�����н�����ֵ�
	 * @param s
	 * @return
	 */
    static TestFragment newInstance(int s) {
        TestFragment newFragment = new TestFragment();
        //��MainActivity�д�������intֵ���н��գ�����TestFragment���е�onCreate�����н���һЩ��ʼ������
        Bundle bundle = new Bundle();
        bundle.putInt("number", s);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    /**
     * Fragment�е�onCreate����һ������������һЩ��ʼ������
     * ��onCreateView�����з��ص�View������һ��Fragment����ʾ�Ķ���
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "TestFragment-----onCreate");
        Bundle args = getArguments();
        num = args != null ? args.getInt("number") : defaultNum;
        //ͨ��Fragment��getActivity()�������Ի��Activity������õ�Context��������Ϊ����ܶ�ط�
        //�õ�������ֱ�ӳ�ʼ��������
        context = getActivity().getApplicationContext();
        
        //��ʼ��Handler����handleMessage������������������ַ���������Ϣ
        handler = new Handler(){
        	public void handleMessage(Message msg) {
        		switch (msg.what) {
        		case MyUrlToJsonToBeautyThread.MESSAGE_GRID_JSON:
        			ArrayList<Beauty> grid_data = (ArrayList<Beauty>) msg.obj;
        			gridAdapter = new GridAdapter(context, this, grid_data);
        			gridView.setAdapter(gridAdapter);
        			break;
        		case MyUrlToJsonToBeautyThread.MESSAGE_LIST_JSON:
        			ArrayList<Beauty> list_data = (ArrayList<Beauty>) msg.obj;
        			listAdapter = new ListAdapter(context, list_data, this);
        			listView.setAdapter(listAdapter);
        			break;
        		case MyUrlToJsonToBeautyThread.MESSAGE_GALLERY_JSON:
        			gallery_data = (ArrayList<Beauty>) msg.obj;
        			gallery = (MyGallery) view.findViewById(R.id.gallery_lay3);
        			galleryAdapter = new MyGalleryAdapter(context, gallery_data, this);
        			gallery.setAdapter(galleryAdapter);
        			//����������gallery�Ĵ��������¼�����Ϊgallery��viewpager���м����¼�
        			//onTouch�����Ǹ���viewpager�������������¼�������gallery���ڵ������ʱ��
        			//viewpager�Ļ����¼��Ǳ���ֹ��
        			gallery.setOnTouchListener(new OnTouchListener() {
        				
        				@Override
        				public boolean onTouch(View v, MotionEvent event) {
        					//��MainActivity���ǽ�viewpager�����static���͵�
        					MainActivity.mPager.requestDisallowInterceptTouchEvent(true);
        					return false;
        				}
        			});
        			break;
        		case Utils.MESSAGE_NOTIFY_GRID:
        			gridAdapter.notifyDataSetChanged();
        			break;
        		case Utils.MESSAGE_NOTIFY_LIST:
        			listAdapter.notifyDataSetChanged();
    				break;
        		case Utils.MESSAGE_NOTIFY_GALLERY:
        			galleryAdapter.notifyDataSetChanged();
        			break;
    			}
        	};
        };
        //����ط���ʼ�����ĸ�Fragment�е����
        initialData();
    }

    /**
     * onCreateView�����в�������Ļ���Fragment�Ľ��棬��������ص�View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "TestFragment-----onCreateView");
        
        switch (num) {
		case MainActivity.CASE_MEINV:
			view = inflater.inflate(R.layout.lay1, container, false);
			gridView = (GridView) view.findViewById(R.id.gridView);
			new MyUrlToJsonToBeautyThread(handler, url, GRID_THREAD).start();
			break;
			
		case MainActivity.CASE_LUOLI:
			view = inflater.inflate(R.layout.lay2, container, false);
			listView = (ListView) view.findViewById(R.id.listView);
			new MyUrlToJsonToBeautyThread(handler, url, LIST_THREAD).start();
			break;
			
		case MainActivity.CASE_QICHE:
			view = inflater.inflate(R.layout.lay3, container, false);
			
			new MyUrlToJsonToBeautyThread(handler, url, GALLERY_THREAD).start();
			break;

		//�������ĸ�Fragment�ǱȽϾ����
		case MainActivity.CASE_LUNTAN:
			view = inflater.inflate(R.layout.lay4, container, false);
			listView_treeview = (ListView) view.findViewById(R.id.listView_treeview);
			//����onCreate�����г�ʼ�������ͨ���Զ���ļ̳���ArrayAdapter��adapter��listview��
			treeViewAdapter = new TreeViewAdapter(context, R.layout.outline,
					mPdfOutlinesCount);
			listView_treeview.setAdapter(treeViewAdapter);
			//listview�ĵ���¼�
			listView_treeview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//super.onListItemClick(parent, view, position, id);
					//���������û�����ֱ��Toast
					if (!mPdfOutlinesCount.get(position).isMhasChild()) {
						//Toast.makeText(this, mPdfOutlinesCount.get(position).getOutlineTitle(), 2000);
						/*int pageNumber;
						Intent i = getIntent();
						PDFOutlineElement element = mPdfOutlinesCount
								.get(position);
						pageNumber = element.getOutlineElement().pageNumber;
						if (pageNumber <= 0) {
							String name = element.getOutlineElement().destName;
							pageNumber = idocviewer.getPageNumberForNameForOutline(name);
							element.getOutlineElement().pageNumber = pageNumber;
							element.getOutlineElement().destName = null;
						}
						i.putExtra("PageNumber", pageNumber);
						setResult(RESULT_OK, i);
						finish();*/

						return;
					}
					
					//�����������չ����
					if (mPdfOutlinesCount.get(position).isExpanded()) {
						//�����չ���ģ���������
						mPdfOutlinesCount.get(position).setExpanded(false);
						DotaElement pdfOutlineElement=mPdfOutlinesCount.get(position);
						ArrayList<DotaElement> temp=new ArrayList<DotaElement>();
						
						for (int i = position+1; i < mPdfOutlinesCount.size(); i++) {
							//����ǳ�������������Ķ���չ��״̬����ô�ڽ���ѭ����ʱ��
							//�����һ���ģ���ô��ѭ�����ڶ����ĵ�ʱ������level����ȵ�
							//���Ի�ֱ��break���forѭ��
							if (pdfOutlineElement.getLevel()>=mPdfOutlinesCount.get(i).getLevel()) {
								break;
							}
							temp.add(mPdfOutlinesCount.get(i));
						}
						
						mPdfOutlinesCount.removeAll(temp);
						
						treeViewAdapter.notifyDataSetChanged();
						/*fileExploreAdapter = new TreeViewAdapter(this, R.layout.outline,
								mPdfOutlinesCount);*/

						//setListAdapter(fileExploreAdapter);
						
					} else {
						mPdfOutlinesCount.get(position).setExpanded(true);
						int level = mPdfOutlinesCount.get(position).getLevel();
						int nextLevel = level + 1;
						
						//����Կ�ʼʱ�����Ļ����ٵ��֮��ֱ���ڵ����λ�ò�������������֪ͨ��������ݼ��ı�
						for (DotaElement pdfOutlineElement : mPdfOutlines) {
							int j=1;
							//getParent()�������ص��ǵ�ǰ��ĸ����id
							//����ÿһ��element�ڳ�ʼ����ʱ��������һ������id�ֶΣ������ڱ����ʱ��
							//ֻ��element�ĸ���id�������������id��ͬ���Ż�����������֪ͨ��������ݼ��ı�
							if (pdfOutlineElement.getParent()==mPdfOutlinesCount.get(position).getId()) {
								pdfOutlineElement.setLevel(nextLevel);
								pdfOutlineElement.setExpanded(false);
								mPdfOutlinesCount.add(position+j, pdfOutlineElement);
								j++;
								Log.d("xiaomi2s", "j="+j);
							}	
							Log.d("xiaomi2a", "xiaomi2a="+j);
						}
						treeViewAdapter.notifyDataSetChanged();
						/*fileExploreAdapter = new TreeViewAdapter(this, R.layout.outline,
								mPdfOutlinesCount);*/

						//setListAdapter(fileExploreAdapter);
					}
				}
			});
			break;
		}
        return view;

    }
    
    private void initialData() {
    	String[] strData = getResources().getStringArray(R.array.dota);
		DotaElement pdfOutlineElement1=new DotaElement("01", strData[0], false	, false, "00", 0,false);
		DotaElement pdfOutlineElement2=new DotaElement("02", strData[1], false	, true, "00", 0,false);
		DotaElement pdfOutlineElement3=new DotaElement("03", strData[2], false	, true, "00", 0,false);
		
		DotaElement pdfOutlineElement4=new DotaElement("04", strData[3], true	, true, "02", 1,false);
		DotaElement pdfOutlineElement5=new DotaElement("05", strData[4], true	, true, "02", 1,false);
		DotaElement pdfOutlineElement6=new DotaElement("06", strData[5], true	, true, "02", 1,false);
		DotaElement pdfOutlineElement7=new DotaElement("07", strData[6], true	, true, "03", 1,false);
		DotaElement pdfOutlineElement8=new DotaElement("08", strData[7], true	, true, "03", 1,false);
		DotaElement pdfOutlineElement9=new DotaElement("09", strData[8], true	, true, "03", 1,false);
		//近卫——智力
		DotaElement pdfOutlineElement10=new DotaElement("10", strData[9], true	, false, "04", 2,false);
		DotaElement pdfOutlineElement11=new DotaElement("11", strData[10], true	, false, "04", 2,false);
		DotaElement pdfOutlineElement12=new DotaElement("12", strData[11], true	, false, "04", 2,false);
		//近卫——力量
		DotaElement pdfOutlineElement13=new DotaElement("13", strData[12], true	, false, "05", 2,false);
		DotaElement pdfOutlineElement14=new DotaElement("14", strData[13], true	, false, "05", 2,false);
		DotaElement pdfOutlineElement15=new DotaElement("15", strData[14], true	, false, "05", 2,false);
		//近卫——敏捷
		DotaElement pdfOutlineElement16=new DotaElement("16", strData[15], true	, false, "06", 2,false);
		DotaElement pdfOutlineElement17=new DotaElement("17", strData[16], true	, false, "06", 2,false);
		DotaElement pdfOutlineElement18=new DotaElement("18", strData[17], true	, false, "06", 2,false);
		//天灾——智力
		DotaElement pdfOutlineElement19=new DotaElement("19", strData[18], true	, false, "07", 2,false);
		DotaElement pdfOutlineElement20=new DotaElement("20", strData[19], true	, false, "07", 2,false);
		DotaElement pdfOutlineElement21=new DotaElement("21", strData[20], true	, false, "07", 2,false);
		//天灾——力量
		DotaElement pdfOutlineElement22=new DotaElement("22", strData[21], true	, false, "08", 2,false);
		DotaElement pdfOutlineElement23=new DotaElement("23", strData[22], true	, false, "08", 2,false);
		DotaElement pdfOutlineElement24=new DotaElement("24", strData[23], true	, false, "08", 2,false);
		//天灾——敏捷
		DotaElement pdfOutlineElement25=new DotaElement("25", strData[24], true	, false, "09", 2,false);
		DotaElement pdfOutlineElement26=new DotaElement("26", strData[25], true	, false, "09", 2,false);
		DotaElement pdfOutlineElement27=new DotaElement("27", strData[26], true	, false, "09", 2,false);
		
		mPdfOutlinesCount.add(pdfOutlineElement1);
		mPdfOutlinesCount.add(pdfOutlineElement2);
		mPdfOutlinesCount.add(pdfOutlineElement3);
	
		mPdfOutlines.add(pdfOutlineElement1);
		mPdfOutlines.add(pdfOutlineElement2);
		mPdfOutlines.add(pdfOutlineElement4);
		mPdfOutlines.add(pdfOutlineElement5);
		mPdfOutlines.add(pdfOutlineElement6);
		mPdfOutlines.add(pdfOutlineElement7);
		mPdfOutlines.add(pdfOutlineElement3);
		mPdfOutlines.add(pdfOutlineElement8);
		mPdfOutlines.add(pdfOutlineElement9);
		mPdfOutlines.add(pdfOutlineElement10);
		mPdfOutlines.add(pdfOutlineElement11);
		mPdfOutlines.add(pdfOutlineElement12);
		mPdfOutlines.add(pdfOutlineElement13);
		mPdfOutlines.add(pdfOutlineElement14);
		mPdfOutlines.add(pdfOutlineElement15);
		mPdfOutlines.add(pdfOutlineElement16);
		mPdfOutlines.add(pdfOutlineElement17);
		mPdfOutlines.add(pdfOutlineElement18);
		mPdfOutlines.add(pdfOutlineElement19);
		mPdfOutlines.add(pdfOutlineElement20);
		mPdfOutlines.add(pdfOutlineElement21);
		mPdfOutlines.add(pdfOutlineElement22);
		mPdfOutlines.add(pdfOutlineElement23);
		mPdfOutlines.add(pdfOutlineElement24);
		mPdfOutlines.add(pdfOutlineElement25);
		mPdfOutlines.add(pdfOutlineElement26);
		mPdfOutlines.add(pdfOutlineElement27);
		
	}
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "TestFragment-----onDestroy");
    }

}
