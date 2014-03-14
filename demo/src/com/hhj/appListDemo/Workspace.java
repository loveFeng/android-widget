package com.hhj.appListDemo;
/*package com.wu;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Workspace extends ViewGroup
  implements DropTarget, DragSource, DragScroller
{
  private static final float BASELINE_FLING_VELOCITY = 2500.0F;
  private static int COUNT_UPDATE_WALLPAPER = 0;
  private static final boolean DEBUG = false;
  private static final String DEFAULTSCREEN = "DefaultScreen";
  private static final boolean ENABLE_GOOGLE_SMOOTH = true;
  private static final float FLING_VELOCITY_INFLUENCE = 0.4F;
  private static final int INVALID_POINTER = 255;
  private static final int INVALID_SCREEN = 64537;
  private static final String ISSCREENCHANGED = "IsScreenChanged";
  public static final String KEY_WKS_TRANSITION_EFFECT = "workspace_transition_effect";
  private static final float NANOTIME_DIV = 1.0E+009F;
  private static final float SMOOTHING_CONSTANT = 0.0F;
  private static final float SMOOTHING_SPEED = 0.75F;
  private static final int SNAP_VELOCITY = 600;
  private static final String TAG = "Launcher2.Workspace";
  private static final int TOUCH_STATE_REST = 0;
  private static final int TOUCH_STATE_SCROLLING = 1;
  public static final int WKS_TRANSITION_CUBE = 4;
  public static final int WKS_TRANSITION_DEFAULT = 0;
  public static final int WKS_TRANSITION_FADE = 2;
  public static final int WKS_TRANSITION_FLIP = 3;
  public static final int WKS_TRANSITION_PHOTOWALL = 6;
  public static final int WKS_TRANSITION_RAND = 0;
  public static final int WKS_TRANSITION_TRANSLATE = 1;
  public static final int WKS_TRANSITION_TYPE_MAX = 7;
  public static final int WKS_TRANSITION_WINDMILL = 5;
  private static boolean canSendMessage;
  private ArrayList<View> availableViews;
  public boolean isAllAppsLoaded;
  private int mActivePointerId;
  private boolean mAllowLongPress;
  private int mCurrentScreen;
  private int mDefaultScreen;
  private DragController mDragController;
  private CellLayout.CellInfo mDragInfo;
  private boolean mFirstLayout = 1;
  private IconCache mIconCache;
  private boolean mIsScreenCountChanged = 0;
  private float mLastMotionX;
  private float mLastMotionY;
  private Launcher mLauncher;
  private View.OnLongClickListener mLongClickListener;
  private int mMaximumVelocity;
  public boolean mNeedWallpaper = 1;
  public boolean mNeedWallpaperRoll = 1;
  private int mNextScreen = 64537;
  PageIndicatorView mPageIndicator;
  private Paint mPaint;
  private WorkspaceOvershootInterpolator mScrollInterpolator;
  private boolean mScrollSpeedKeep;
  private Scroller mScroller;
  private int mScrollingSpeed;
  private float mSmoothingTime;
  private int[] mTargetCell = null;
  private int[] mTempCell;
  private int[] mTempEstimate;
  private int mTouchSlop;
  private int mTouchState = 0;
  private float mTouchX;
  private CellLayout.CellInfo mVacantCache = null;
  private VelocityTracker mVelocityTracker;
  private Bitmap mWallpaper;
  private int mWallpaperHeight;
  private boolean mWallpaperLoaded;
  private final WallpaperManager mWallpaperManager;
  private float mWallpaperOffset;
  public boolean mWallpaperOverflow = 0;
  private int mWallpaperWidth;
  private boolean m_isCycle;
  private int m_savedTransitionType;
  private int m_transitionType;
  private int screenHeight;
  private int screenWidth;
  private ArrayList<View> unAvailableViews;

  static
  {
    double d = Math.log(0.75D);
    SMOOTHING_CONSTANT = (float)(0.016D / d);
    canSendMessage = 1;
    WKS_TRANSITION_DEFAULT = SystemProperties.getInt("ro.tyd.default.wks", 0);
  }

  public Workspace(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public Workspace(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {}

  private void _snapToScreen(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = getChildCount() - 1;
    int j = Math.min(paramInt1, i);
    paramInt1 = Math.max(0, j);
    clearVacantCache();
    int k = this.mCurrentScreen;
    enableChildrenCache(k, paramInt1);
    this.mNextScreen = paramInt1;
    View localView1 = getFocusedChild();
    if (localView1 != null)
    {
      int l = this.mCurrentScreen;
      if (paramInt1 != l)
      {
        int i1 = this.mCurrentScreen;
        View localView2 = getChildAt(i1);
        if (localView1 == localView2)
          localView1.clearFocus();
      }
    }
    int i2 = this.mCurrentScreen;
    int i3 = Math.abs(paramInt1 - i2);
    int i4 = Math.max(1, i3);
    int i5 = getWidth();
    int i6 = paramInt1 * i5;
    int i7 = this.mScrollX;
    int i8 = i6 - i7;
    int i9 = (i4 + 1) * 100;
    if (!this.mScroller.isFinished())
      this.mScroller.abortAnimation();
    paramInt2 = Math.abs(paramInt2);
    if (paramInt2 > 0)
    {
      float f1 = i9;
      float f2 = i9;
      float f3 = paramInt2 / 2500.0F;
      float f4 = f2 / f3 * 0.4F;
      i9 = (int)(f1 + f4);
    }
    while (true)
    {
      controlMTKWidget(paramInt1);
      boolean bool = awakenScrollBars(i9);
      Scroller localScroller = this.mScroller;
      int i10 = this.mScrollX;
      int i11 = 0;
      localScroller.startScroll(i10, 0, i8, i11, i9);
      invalidate();
      return;
      i9 += 100;
    }
  }

  private void acquireVelocityTrackerAndAddMovement(MotionEvent paramMotionEvent)
  {
    if (this.mVelocityTracker == null)
    {
      VelocityTracker localVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker = localVelocityTracker;
    }
    this.mVelocityTracker.addMovement(paramMotionEvent);
  }

  private void changeCellLayoutBackground(boolean paramBoolean)
  {
    int i = getChildCount();
    int j = 0;
    if (j >= i)
      label7: return;
    View localView = getChildAt(j);
    if (paramBoolean)
      localView.setBackgroundResource(2130837541);
    while (true)
    {
      j += 1;
      break label7:
      localView.setBackgroundDrawable(null);
    }
  }

  private void clearVacantCache()
  {
    if (this.mVacantCache == null)
      return;
    this.mVacantCache.clearVacantCells();
    this.mVacantCache = null;
  }

  private int[] estimateDropCell(int paramInt1, int paramInt2, int paramInt3, int paramInt4, View paramView, CellLayout paramCellLayout, int[] paramArrayOfInt)
  {
    if (this.mVacantCache == null)
    {
      CellLayout.CellInfo localCellInfo1 = paramCellLayout.findAllVacantCells(null, paramView);
      this.mVacantCache = localCellInfo1;
    }
    CellLayout.CellInfo localCellInfo2 = this.mVacantCache;
    CellLayout localCellLayout = paramCellLayout;
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    int l = paramInt4;
    int[] arrayOfInt = paramArrayOfInt;
    return localCellLayout.findNearestVacantArea(i, j, k, l, localCellInfo2, arrayOfInt);
  }

  private CellLayout getCurrentDropLayout()
  {
    if (this.mScroller.isFinished());
    for (int i = this.mCurrentScreen; ; i = this.mNextScreen)
    {
      int j = getChildCount();
      int k = (i + j) % j;
      return (CellLayout)getChildAt(k);
    }
  }

  private void initWorkspace()
  {
    Context localContext = getContext();
    WorkspaceOvershootInterpolator localWorkspaceOvershootInterpolator1 = new WorkspaceOvershootInterpolator();
    this.mScrollInterpolator = localWorkspaceOvershootInterpolator1;
    WorkspaceOvershootInterpolator localWorkspaceOvershootInterpolator2 = this.mScrollInterpolator;
    Scroller localScroller = new Scroller(localContext, localWorkspaceOvershootInterpolator2);
    this.mScroller = localScroller;
    int i = this.mDefaultScreen;
    this.mCurrentScreen = i;
    Launcher.setScreen(this.mCurrentScreen);
    IconCache localIconCache = ((LauncherApplication)localContext.getApplicationContext()).getIconCache();
    this.mIconCache = localIconCache;
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(getContext());
    int j = localViewConfiguration.getScaledTouchSlop();
    this.mTouchSlop = j;
    int k = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mMaximumVelocity = k;
    DisplayMetrics localDisplayMetrics = localContext.getResources().getDisplayMetrics();
    int l = localDisplayMetrics.widthPixels;
    this.screenWidth = l;
    int i1 = localDisplayMetrics.heightPixels;
    this.screenHeight = i1;
  }

  private boolean isScreenNoValid(int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = getChildCount();
      if (paramInt >= i);
    }
    for (int j = 1; ; j = 0)
      return j;
  }

  private void log(String paramString)
  {
    Utilities.log(super.getClass().getName(), paramString);
  }

  private void onDropExternal(int paramInt1, int paramInt2, Object paramObject, CellLayout paramCellLayout)
  {
    Workspace localWorkspace = this;
    int i = paramInt1;
    int j = paramInt2;
    Object localObject = paramObject;
    CellLayout localCellLayout = paramCellLayout;
    localWorkspace.onDropExternal(i, j, localObject, localCellLayout, 0);
  }

  private void onDropExternal(int paramInt1, int paramInt2, Object paramObject, CellLayout paramCellLayout, boolean paramBoolean)
  {
    Object localObject1 = (ItemInfo)paramObject;
    Object localObject2 = null;
    switch (((ItemInfo)localObject1).itemType)
    {
    case 3:
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unknown item type: ");
      int i = ((ItemInfo)localObject1).itemType;
      String str = i;
      throw new IllegalStateException(str);
    case 0:
    case 1:
      if ((((ItemInfo)localObject1).container == 65535L) && (localObject1 instanceof ApplicationInfo))
      {
        ShortcutInfo localShortcutInfo1 = new com/android/launcherics/ShortcutInfo;
        ApplicationInfo localApplicationInfo1 = (ApplicationInfo)localObject1;
        ShortcutInfo localShortcutInfo2 = localShortcutInfo1;
        ApplicationInfo localApplicationInfo2 = localApplicationInfo1;
        localShortcutInfo2.<init>(localApplicationInfo2);
        localObject1 = localShortcutInfo1;
      }
      Launcher localLauncher1 = this.mLauncher;
      ShortcutInfo localShortcutInfo3 = (ShortcutInfo)localObject1;
      Launcher localLauncher2 = localLauncher1;
      int j = 2130903045;
      CellLayout localCellLayout1 = paramCellLayout;
      ShortcutInfo localShortcutInfo4 = localShortcutInfo3;
      localObject2 = localLauncher2.createShortcut(j, localCellLayout1, localShortcutInfo4);
    case 2:
    case 4:
    }
    while (localObject2 == null)
    {
      label191: return;
      Launcher localLauncher3 = this.mLauncher;
      int k = this.mCurrentScreen;
      Workspace localWorkspace1 = this;
      int l = k;
      ViewGroup localViewGroup1 = (ViewGroup)localWorkspace1.getChildAt(l);
      UserFolderInfo localUserFolderInfo1 = (UserFolderInfo)localObject1;
      int i1 = 2130903049;
      Launcher localLauncher4 = localLauncher3;
      ViewGroup localViewGroup2 = localViewGroup1;
      UserFolderInfo localUserFolderInfo2 = localUserFolderInfo1;
      localObject2 = FolderIcon.fromXml(i1, localLauncher4, localViewGroup2, localUserFolderInfo2);
      continue;
      int[] arrayOfInt1 = this.mTargetCell;
      Workspace localWorkspace2 = this;
      int i2 = paramInt1;
      int i3 = paramInt2;
      CellLayout localCellLayout2 = paramCellLayout;
      int[] arrayOfInt2 = localWorkspace2.estimateDropCell(i2, i3, 1, 1, null, localCellLayout2, arrayOfInt1);
      this.mTargetCell = arrayOfInt2;
      Launcher localLauncher5 = this.mLauncher;
      WidgetInfo localWidgetInfo = (WidgetInfo)paramObject;
      int i4 = this.mCurrentScreen;
      int[] arrayOfInt3 = this.mTargetCell;
      int i5 = paramInt1;
      int i6 = paramInt2;
      localLauncher5.addAppWidgetDrop(localWidgetInfo, i4, arrayOfInt3, i5, i6);
    }
    if (paramBoolean);
    for (int i7 = 0; ; i7 = -1)
    {
      CellLayout localCellLayout3 = paramCellLayout;
      Object localObject3 = localObject2;
      int i8 = i7;
      localCellLayout3.addView(localObject3, i8);
      ((View)localObject2).setHapticFeedbackEnabled(0);
      View.OnLongClickListener localOnLongClickListener = this.mLongClickListener;
      ((View)localObject2).setOnLongClickListener(localOnLongClickListener);
      if (localObject2 instanceof DragScroller)
      {
        DragController localDragController = this.mDragController;
        DropTarget localDropTarget = (DragScroller)localObject2;
        localDragController.addDropTarget(localDropTarget);
      }
      int[] arrayOfInt4 = this.mTargetCell;
      Workspace localWorkspace3 = this;
      int i9 = paramInt1;
      int i10 = paramInt2;
      CellLayout localCellLayout4 = paramCellLayout;
      int[] arrayOfInt5 = localWorkspace3.estimateDropCell(i9, i10, 1, 1, (View)localObject2, localCellLayout4, arrayOfInt4);
      this.mTargetCell = arrayOfInt5;
      int[] arrayOfInt6 = this.mTargetCell;
      CellLayout localCellLayout5 = paramCellLayout;
      Object localObject4 = localObject2;
      int[] arrayOfInt7 = arrayOfInt6;
      localCellLayout5.onDropChild(localObject4, arrayOfInt7);
      CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)((View)localObject2).getLayoutParams();
      Launcher localLauncher6 = this.mLauncher;
      int i11 = this.mCurrentScreen;
      int i12 = localLayoutParams.cellX;
      int i13 = localLayoutParams.cellY;
      Object localObject5 = localObject1;
      LauncherModel.addOrMoveItemInDatabase(localLauncher6, localObject5, 65436L, i11, i12, i13);
      break label191:
    }
  }

  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = (paramMotionEvent.getAction() & 0xFF00) >> 8;
    int j = paramMotionEvent.getPointerId(i);
    int k = this.mActivePointerId;
    if (j == k)
      if (i != 0)
        break label94;
    for (int l = 1; ; l = 0)
    {
      float f1 = paramMotionEvent.getX(l);
      this.mLastMotionX = f1;
      float f2 = paramMotionEvent.getY(l);
      this.mLastMotionY = f2;
      int i1 = paramMotionEvent.getPointerId(l);
      this.mActivePointerId = i1;
      if (this.mVelocityTracker != null)
        this.mVelocityTracker.clear();
      label94: return;
    }
  }

  private void releaseVelocityTracker()
  {
    if (this.mVelocityTracker == null)
      return;
    this.mVelocityTracker.recycle();
    this.mVelocityTracker = null;
  }

  private void snapToScreen(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = getChildCount();
    int j = getChildCount() - 0;
    int k = Math.min(paramInt1, j);
    paramInt1 = Math.max(-1, k);
    clearVacantCache();
    int l = this.mCurrentScreen;
    this.mNextScreen = paramInt1;
    int i1 = (this.mNextScreen + i) % i;
    int i2 = this.mCurrentScreen;
    enableChildrenCache(i2, i1);
    StringBuilder localStringBuilder1 = new StringBuilder().append("*****mNextScreen******: ");
    int i3 = this.mNextScreen;
    String str1 = i3;
    int i4 = Log.i("Launcher2.Workspace", str1);
    PageIndicatorView localPageIndicatorView1 = this.mPageIndicator;
    int i5 = getChildCount();
    localPageIndicatorView1.setCount(i5, i1);
    View localView1 = getFocusedChild();
    if (localView1 != null)
    {
      int i6 = this.mCurrentScreen;
      if (i1 != i6)
      {
        int i7 = this.mCurrentScreen;
        View localView2 = getChildAt(i7);
        if (localView1 == localView2)
          localView1.clearFocus();
      }
    }
    int i8 = this.mCurrentScreen;
    int i9 = Math.abs(paramInt1 - i8);
    int i10 = getWidth() * paramInt1;
    int i11 = getScrollX();
    int i12 = i10 - i11;
    if (!this.mScroller.isFinished())
      this.mScroller.abortAnimation();
    int i13;
    label247: int i15;
    label268: int i19;
    if (i9 > 0)
    {
      i13 = 700;
      int i14 = this.mCurrentScreen;
      if (Math.abs(i1 - i14) <= 0)
        break label432;
      i15 = 1;
      this.mNeedWallpaperRoll = i15;
      StringBuilder localStringBuilder2 = new StringBuilder().append("getScrollX():  ");
      int i16 = getScrollX();
      String str2 = i16 + "     deltaX:   " + i12;
      int i17 = Log.i("Launcher2.Workspace", str2);
      Scroller localScroller = this.mScroller;
      int i18 = getScrollX();
      localScroller.startScroll(i18, 0, i12, 0, i13);
      requestCellLayout(0);
      if (this.mNextScreen != -1)
        break label439;
      i19 = getChildCount() - 1;
    }
    label432: label439: int i27;
    for (this.mCurrentScreen = i19; ; this.mCurrentScreen = i27)
    {
      while (true)
      {
        PageIndicatorView localPageIndicatorView2 = this.mPageIndicator;
        int i20 = this.mCurrentScreen;
        localPageIndicatorView2.scrollToScreen(i13, i20);
        Launcher localLauncher = this.mLauncher;
        int i21 = this.mCurrentScreen;
        localLauncher.updateAllAppsViewTitle(l, i21);
        return;
        i13 = 400;
        break label247:
        i15 = 0;
        break label268:
        int i22 = this.mNextScreen;
        int i23 = getChildCount();
        if (i22 != i23)
          break;
        this.mCurrentScreen = 0;
      }
      int i24 = this.mNextScreen;
      int i25 = getChildCount() - 1;
      int i26 = Math.min(i24, i25);
      i27 = Math.max(0, i26);
    }
  }

  private void transitionDrawChild(Canvas paramCanvas, View paramView, int paramInt)
  {
    int i = paramCanvas.save();
    paramView.computeScroll();
    int j = paramView.getWidth();
    int k = paramView.getHeight();
    int l = paramView.getScrollX();
    int i1 = paramView.getScrollY();
    float f1 = paramInt - l;
    float f2 = paramView.getTop() - i1;
    paramCanvas.translate(f1, f2);
    int i2 = l + j;
    int i3 = i1 + k;
    boolean bool = paramCanvas.clipRect(l, i1, i2, i3);
    paramView.draw(paramCanvas);
    paramCanvas.restore();
  }

  private void transitionDrawChild(Canvas paramCanvas, View paramView, int paramInt1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, int paramInt2)
  {
    Camera localCamera1 = new Camera();
    Matrix localMatrix1 = new Matrix();
    int i = paramCanvas.save();
    paramView.computeScroll();
    int j = paramView.getWidth();
    int k = paramView.getHeight();
    int l = paramView.getScrollX();
    int i1 = paramView.getScrollY();
    float f1 = paramInt1 - l;
    float f2 = paramView.getTop() - i1;
    Canvas localCanvas1 = paramCanvas;
    float f3 = f1;
    float f4 = f2;
    localCanvas1.translate(f3, f4);
    localCamera1.save();
    Camera localCamera2 = localCamera1;
    float f5 = paramFloat1;
    localCamera2.rotateY(f5);
    Camera localCamera3 = localCamera1;
    float f6 = paramFloat2;
    localCamera3.rotateZ(f6);
    localCamera1.getMatrix(localMatrix1);
    localCamera1.restore();
    float f7 = -paramFloat5;
    float f8 = -paramFloat6;
    boolean bool1 = localMatrix1.preTranslate(f7, f8);
    Matrix localMatrix2 = localMatrix1;
    float f9 = paramFloat3;
    float f10 = paramFloat3;
    boolean bool2 = localMatrix2.postScale(f9, f10);
    float f11 = paramFloat5 + paramFloat4;
    Matrix localMatrix3 = localMatrix1;
    float f12 = f11;
    float f13 = paramFloat6;
    boolean bool3 = localMatrix3.postTranslate(f12, f13);
    Canvas localCanvas2 = paramCanvas;
    Matrix localMatrix4 = localMatrix1;
    localCanvas2.concat(localMatrix4);
    float f14 = l;
    float f15 = i1;
    float f16 = l + j;
    float f17 = i1 + k;
    Canvas localCanvas3 = paramCanvas;
    int i2 = paramInt2;
    int i3 = localCanvas3.saveLayerAlpha(f14, f15, f16, f17, i2, 20);
    int i4 = l + j;
    int i5 = i1 + k;
    Canvas localCanvas4 = paramCanvas;
    int i6 = l;
    int i7 = i1;
    int i8 = i4;
    int i9 = i5;
    boolean bool4 = localCanvas4.clipRect(i6, i7, i8, i9);
    PaintFlagsDrawFilter localPaintFlagsDrawFilter1 = new PaintFlagsDrawFilter(0, 2);
    Canvas localCanvas5 = paramCanvas;
    PaintFlagsDrawFilter localPaintFlagsDrawFilter2 = localPaintFlagsDrawFilter1;
    localCanvas5.setDrawFilter(localPaintFlagsDrawFilter2);
    View localView = paramView;
    Canvas localCanvas6 = paramCanvas;
    localView.draw(localCanvas6);
    Canvas localCanvas7 = paramCanvas;
    int i10 = i;
    localCanvas7.restoreToCount(i10);
  }

  private void updateWallpaperOffset()
  {
    if ((!this.mWallpaperOverflow) || (!this.mNeedWallpaper))
      return;
    int i = getChildCount() - 1;
    int j = getChildAt(i).getRight();
    int k = this.mRight;
    int l = this.mLeft;
    int i1 = k - l;
    int i2 = j - i1;
    updateWallpaperOffset(i2);
  }

  private void updateWallpaperOffset(int paramInt)
  {
    int i;
    if ((this.mWallpaperOverflow) && (this.mNeedWallpaper) && (getWindowToken() != null))
    {
      i = getChildCount();
      int j = this.mScroller.getCurrX();
      if (j < 0)
        break label119;
      int k = getWidth();
      int l = i - 1;
      int i1 = k * l;
      if (j > i1)
        break label119;
      WallpaperManager localWallpaperManager1 = this.mWallpaperManager;
      IBinder localIBinder1 = getWindowToken();
      float f1 = this.mScrollX;
      float f2 = paramInt;
      float f3 = Math.min(f1 / f2, 1.0F);
      float f4 = Math.max(0.0F, f3);
      localWallpaperManager1.setWallpaperOffsets(localIBinder1, f4, 0.0F);
    }
    while (true)
    {
      return;
      label119: if (!this.mNeedWallpaperRoll)
        continue;
      float f5 = i - 1;
      float f6 = 1.0F / f5;
      float f7 = this.mScrollX;
      float f8 = paramInt;
      float f9 = f7 / f8;
      if (f9 > 1.0F)
      {
        WallpaperManager localWallpaperManager2 = this.mWallpaperManager;
        IBinder localIBinder2 = getWindowToken();
        float f10 = 1.0F + f6 - f9;
        float f11 = i - 1;
        float f12 = Math.min(f10 * f11, 1.0F);
        localWallpaperManager2.setWallpaperOffsets(localIBinder2, f12, 0.0F);
      }
      if (f9 >= 0.0F)
        continue;
      WallpaperManager localWallpaperManager3 = this.mWallpaperManager;
      IBinder localIBinder3 = getWindowToken();
      float f13 = -f9;
      float f14 = i - 1;
      float f15 = Math.max(f13 * f14, 0.0F);
      localWallpaperManager3.setWallpaperOffsets(localIBinder3, f15, 0.0F);
    }
  }

  public void __refreshWorkspace(int paramInt)
  {
    this.availableViews.clear();
    this.unAvailableViews.clear();
    int i = getChildCount();
    int j = 0;
    if (j >= i)
      label21: return;
    View localView = super.getChildAt(j);
    View.OnLongClickListener localOnLongClickListener = this.mLongClickListener;
    localView.setOnLongClickListener(localOnLongClickListener);
    if (j < paramInt)
      boolean bool1 = this.availableViews.add(localView);
    while (true)
    {
      j += 1;
      break label21:
      boolean bool2 = this.unAvailableViews.add(localView);
    }
  }

  public void _computeScroll()
  {
    if (this.mScroller.computeScrollOffset())
    {
      int i = this.mScroller.getCurrX();
      this.mScrollX = i;
      float f1 = i;
      this.mTouchX = f1;
      float f2 = (float)System.nanoTime() / 1.0E+009F;
      this.mSmoothingTime = f2;
      int j = this.mScroller.getCurrY();
      this.mScrollY = j;
      updateWallpaperOffset();
      postInvalidate();
    }
    while (true)
    {
      return;
      if (this.mNextScreen != 64537)
      {
        int k = this.mNextScreen;
        View localView1 = getChildAt(k);
        View localView2 = searchIMTKWidget(localView1);
        if (localView2 != null)
        {
          IMTKWidget localIMTKWidget = (IMTKWidget)localView2;
          int l = this.mNextScreen;
          localIMTKWidget.moveIn(l);
          canSendMessage = 1;
        }
        int i1 = this.mNextScreen;
        int i2 = getChildCount() - 1;
        int i3 = Math.min(i1, i2);
        int i4 = Math.max(0, i3);
        this.mCurrentScreen = i4;
        Launcher.setScreen(this.mCurrentScreen);
        this.mNextScreen = 64537;
        clearChildrenCache();
      }
      if (this.mTouchState != 1)
        continue;
      float f3 = (float)System.nanoTime() / 1.0E+009F;
      float f4 = this.mSmoothingTime;
      float f5 = f3 - f4;
      float f6 = SMOOTHING_CONSTANT;
      float f7 = (float)Math.exp(f5 / f6);
      float f8 = this.mTouchX;
      float f9 = this.mScrollX;
      float f10 = f8 - f9;
      float f11 = this.mScrollX;
      float f12 = f10 * f7;
      int i5 = (int)(f11 + f12);
      this.mScrollX = i5;
      this.mSmoothingTime = f3;
      if ((f10 <= 1.0F) && (f10 >= -1.0F))
        continue;
      updateWallpaperOffset();
      postInvalidate();
    }
  }

  public View _getChildAt(int paramInt)
  {
    int i = this.availableViews.size();
    int j = this.unAvailableViews.size();
    int k = i + j;
    if (paramInt >= k);
    for (View localView = super.getChildAt(paramInt); ; localView = (View)this.availableViews.get(paramInt))
      while (true)
      {
        return localView;
        int l = this.availableViews.size();
        if (paramInt < l)
          break;
        localView = (View)this.unAvailableViews.get(0);
      }
  }

  public int _getChildCount()
  {
    int i = this.availableViews.size();
    int j = this.unAvailableViews.size();
    if (i + j <= 0);
    for (int k = super.getChildCount(); ; k = this.availableViews.size())
      return k;
  }

  public boolean _onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = this.mLauncher.isWorkspaceLocked();
    boolean bool2 = this.mLauncher.isAllAppsVisible();
    if ((bool1) || (bool2));
    for (int i = 0; ; i = 0)
    {
      label27: int j;
      while (true)
      {
        return i;
        j = paramMotionEvent.getAction();
        if ((j != 2) || (this.mTouchState == 0))
          break;
        i = 1;
      }
      acquireVelocityTrackerAndAddMovement(paramMotionEvent);
      switch (j & 0xFF)
      {
      case 4:
      case 5:
      default:
      case 2:
      case 0:
      case 1:
      case 3:
      case 6:
      }
      while (this.mTouchState != 0)
      {
        label108: i = 1;
        break label27:
        int k = this.mActivePointerId;
        MotionEvent localMotionEvent1 = paramMotionEvent;
        int l = k;
        int i1 = localMotionEvent1.findPointerIndex(l);
        MotionEvent localMotionEvent2 = paramMotionEvent;
        int i2 = i1;
        float f1 = localMotionEvent2.getX(i2);
        MotionEvent localMotionEvent3 = paramMotionEvent;
        int i3 = i1;
        float f2 = localMotionEvent3.getY(i3);
        float f3 = this.mLastMotionX;
        int i4 = (int)Math.abs(f1 - f3);
        float f4 = this.mLastMotionY;
        int i5 = (int)Math.abs(f2 - f4);
        int i6 = this.mTouchSlop;
        int i7 = i4;
        int i8 = i6;
        int i9;
        if (i7 > i8)
        {
          i9 = 1;
          label233: int i10 = i5;
          int i11 = i6;
          if (i10 <= i11)
            break label398;
        }
        for (int i12 = 1; ; i12 = 0)
        {
          if ((i9 != 0) || (i12 != 0));
          if (i9 != 0)
          {
            int i13 = 1;
            this.mTouchState = i13;
            float f5 = f1;
            this.mLastMotionX = f5;
            float f6 = this.mScrollX;
            this.mTouchX = f6;
            float f7 = (float)System.nanoTime() / 1.0E+009F;
            this.mSmoothingTime = f7;
            int i14 = this.mCurrentScreen - 1;
            int i15 = this.mCurrentScreen + 1;
            Workspace localWorkspace1 = this;
            int i16 = i14;
            int i17 = i15;
            localWorkspace1.enableChildrenCache(i16, i17);
          }
          if (this.mAllowLongPress);
          int i18 = 0;
          this.mAllowLongPress = i18;
          int i19 = this.mCurrentScreen;
          Workspace localWorkspace2 = this;
          int i20 = i19;
          localWorkspace2.getChildAt(i20).cancelLongPress();
          break label108:
          i9 = 0;
          label398: break label233:
        }
        float f8 = paramMotionEvent.getX();
        float f9 = paramMotionEvent.getY();
        float f10 = f8;
        this.mLastMotionX = f10;
        float f11 = f9;
        this.mLastMotionY = f11;
        MotionEvent localMotionEvent4 = paramMotionEvent;
        int i21 = 0;
        int i22 = localMotionEvent4.getPointerId(i21);
        this.mActivePointerId = i22;
        int i23 = 1;
        this.mAllowLongPress = i23;
        if (this.mScroller.isFinished());
        for (i = 0; ; i = 1)
        {
          int i24 = i;
          this.mTouchState = i24;
          break label108:
        }
        if (this.mTouchState != 1)
        {
          int i25 = this.mCurrentScreen;
          Workspace localWorkspace3 = this;
          int i26 = i25;
          if (!((CellLayout)localWorkspace3.getChildAt(i26)).lastDownOnOccupiedCell())
          {
            int[] arrayOfInt1 = this.mTempCell;
            Workspace localWorkspace4 = this;
            int[] arrayOfInt2 = arrayOfInt1;
            localWorkspace4.getLocationOnScreen(arrayOfInt2);
            if ((this.mWallpaperOverflow) && (this.mNeedWallpaper))
            {
              int i27 = this.mActivePointerId;
              MotionEvent localMotionEvent5 = paramMotionEvent;
              int i28 = i27;
              int i29 = localMotionEvent5.findPointerIndex(i28);
              if (i29 >= 0)
              {
                WallpaperManager localWallpaperManager = this.mWallpaperManager;
                IBinder localIBinder = getWindowToken();
                int i30 = this.mTempCell[0];
                MotionEvent localMotionEvent6 = paramMotionEvent;
                int i31 = i29;
                int i32 = (int)localMotionEvent6.getX(i31);
                int i33 = i30 + i32;
                int i34 = this.mTempCell[1];
                MotionEvent localMotionEvent7 = paramMotionEvent;
                int i35 = i29;
                int i36 = (int)localMotionEvent7.getY(i35);
                int i37 = i34 + i36;
                localWallpaperManager.sendWallpaperCommand(localIBinder, "android.wallpaper.tap", i33, i37, 0, null);
              }
            }
          }
        }
        clearChildrenCache();
        int i38 = 0;
        this.mTouchState = i38;
        int i39 = -1;
        this.mActivePointerId = i39;
        int i40 = 0;
        this.mAllowLongPress = i40;
        releaseVelocityTracker();
        continue;
        onSecondaryPointerUp(paramMotionEvent);
      }
    }
  }

  public boolean _onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mLauncher.isWorkspaceLocked());
    for (int i = 0; ; i = 0)
    {
      label12: return i;
      if (!this.mLauncher.isAllAppsVisible())
        break;
      if (!this.mScroller.isFinished())
        this.mScroller.abortAnimation();
      int j = this.mCurrentScreen;
      Workspace localWorkspace1 = this;
      int k = j;
      localWorkspace1.snapToScreen(k);
    }
    acquireVelocityTrackerAndAddMovement(paramMotionEvent);
    switch (paramMotionEvent.getAction() & 0xFF)
    {
    case 4:
    case 5:
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    case 6:
    }
    while (true)
    {
      label120: i = 1;
      break label12:
      if (!this.mScroller.isFinished())
        this.mScroller.abortAnimation();
      float f1 = paramMotionEvent.getX();
      this.mLastMotionX = f1;
      MotionEvent localMotionEvent1 = paramMotionEvent;
      int l = 0;
      int i1 = localMotionEvent1.getPointerId(l);
      this.mActivePointerId = i1;
      int i2 = this.mTouchState;
      int i3 = 1;
      if (i2 != i3)
        continue;
      int i4 = this.mCurrentScreen - 1;
      int i5 = this.mCurrentScreen + 1;
      Workspace localWorkspace2 = this;
      int i6 = i4;
      int i7 = i5;
      localWorkspace2.enableChildrenCache(i6, i7);
      continue;
      if ((canSendMessage) && (!this.mDragController.isDraging()))
      {
        int i8 = this.mCurrentScreen;
        Workspace localWorkspace3 = this;
        int i9 = i8;
        View localView1 = localWorkspace3.getChildAt(i9);
        Workspace localWorkspace4 = this;
        View localView2 = localView1;
        View localView3 = localWorkspace4.searchIMTKWidget(localView2);
        if (localView3 != null)
        {
          IMTKWidget localIMTKWidget1 = (IMTKWidget)localView3;
          int i10 = this.mCurrentScreen;
          IMTKWidget localIMTKWidget2 = localIMTKWidget1;
          int i11 = i10;
          if (!localIMTKWidget2.moveOut(i11))
            i = 1;
          canSendMessage = 0;
        }
      }
      int i12 = this.mTouchState;
      int i13 = 1;
      if (i12 != i13)
        continue;
      int i14 = this.mActivePointerId;
      MotionEvent localMotionEvent2 = paramMotionEvent;
      int i15 = i14;
      int i16 = localMotionEvent2.findPointerIndex(i15);
      MotionEvent localMotionEvent3 = paramMotionEvent;
      int i17 = i16;
      float f2 = localMotionEvent3.getX(i17);
      float f3 = this.mLastMotionX - f2;
      float f4 = f2;
      this.mLastMotionX = f4;
      if (f3 < 0.0F)
      {
        if (this.mTouchX > 0.0F)
        {
          float f5 = this.mTouchX;
          float f6 = -this.mTouchX;
          float f7 = f3;
          float f8 = Math.max(f6, f7);
          float f9 = f5 + f8;
          this.mTouchX = f9;
          float f10 = (float)System.nanoTime() / 1.0E+009F;
          this.mSmoothingTime = f10;
          invalidate();
        }
        PageIndicatorView localPageIndicatorView1 = this.mPageIndicator;
        int i18 = (int)f3;
        localPageIndicatorView1.scrollX(i18);
      }
      if (f3 > 0.0F)
      {
        int i19 = getChildCount() - 1;
        Workspace localWorkspace5 = this;
        int i20 = i19;
        int i21 = localWorkspace5.getChildAt(i20).getRight();
        int i22 = this.mScrollX;
        int i23 = i21 - i22;
        int i24 = getWidth();
        int i25 = i23 - i24;
        if (i25 <= 0)
          continue;
        float f11 = this.mTouchX;
        float f12 = i25;
        float f13 = f3;
        float f14 = Math.min(f12, f13);
        float f15 = f11 + f14;
        this.mTouchX = f15;
        float f16 = (float)System.nanoTime() / 1.0E+009F;
        this.mSmoothingTime = f16;
        invalidate();
        PageIndicatorView localPageIndicatorView2 = this.mPageIndicator;
        int i26 = i25;
        localPageIndicatorView2.scrollX(i26);
      }
      boolean bool = awakenScrollBars();
      continue;
      int i27 = this.mTouchState;
      int i28 = 1;
      int i32;
      int i36;
      float f21;
      int i39;
      if (i27 == i28)
      {
        VelocityTracker localVelocityTracker1 = this.mVelocityTracker;
        float f17 = this.mMaximumVelocity;
        VelocityTracker localVelocityTracker2 = localVelocityTracker1;
        int i29 = 1000;
        float f18 = f17;
        localVelocityTracker2.computeCurrentVelocity(i29, f18);
        int i30 = this.mActivePointerId;
        VelocityTracker localVelocityTracker3 = localVelocityTracker1;
        int i31 = i30;
        i32 = (int)localVelocityTracker3.getXVelocity(i31);
        int i33 = getWidth();
        int i34 = this.mScrollX;
        int i35 = i33 / 2;
        i36 = (i34 + i35) / i33;
        float f19 = this.mScrollX;
        float f20 = i33;
        f21 = f19 / f20;
        int i37 = i32;
        int i38 = 600;
        if ((i37 <= i38) || (this.mCurrentScreen <= 0))
          break label898;
        float f22 = i36;
        if (f21 >= f22)
          break label889;
        i39 = this.mCurrentScreen - 1;
        label819: int i40 = i36;
        int i41 = i39;
        int i42 = Math.min(i40, i41);
        Workspace localWorkspace6 = this;
        int i43 = i42;
        int i44 = i32;
        int i45 = 1;
        localWorkspace6.snapToScreen(i43, i44, i45);
      }
      while (true)
      {
        label862: int i46 = 0;
        this.mTouchState = i46;
        int i47 = 65535;
        this.mActivePointerId = i47;
        releaseVelocityTracker();
        break label120:
        label889: i39 = this.mCurrentScreen;
        break label819:
        label898: int i48 = i32;
        int i49 = 64936;
        if (i48 < i49)
        {
          int i50 = this.mCurrentScreen;
          int i51 = getChildCount() - 1;
          int i52 = i50;
          int i53 = i51;
          if (i52 < i53)
          {
            float f23 = i36;
            if (f21 > f23);
            for (i39 = this.mCurrentScreen + 1; ; i39 = this.mCurrentScreen)
            {
              int i54 = i36;
              int i55 = i39;
              int i56 = Math.max(i54, i55);
              Workspace localWorkspace7 = this;
              int i57 = i56;
              int i58 = i32;
              int i59 = 1;
              localWorkspace7.snapToScreen(i57, i58, i59);
              break label862:
            }
          }
        }
        Workspace localWorkspace8 = this;
        int i60 = i36;
        int i61 = 0;
        int i62 = 1;
        localWorkspace8.snapToScreen(i60, i61, i62);
      }
      int i63 = this.mTouchState;
      int i64 = 1;
      if (i63 == i64)
      {
        int i65 = getWidth();
        int i66 = this.mScrollX;
        int i67 = i65 / 2;
        int i68 = (i66 + i67) / i65;
        Workspace localWorkspace9 = this;
        int i69 = i68;
        int i70 = 0;
        int i71 = 1;
        localWorkspace9.snapToScreen(i69, i70, i71);
      }
      int i72 = 0;
      this.mTouchState = i72;
      int i73 = 65535;
      this.mActivePointerId = i73;
      releaseVelocityTracker();
      continue;
      onSecondaryPointerUp(paramMotionEvent);
    }
  }

  void _snapToScreen(int paramInt)
  {
    snapToScreen(paramInt, 0, 0);
  }

  public boolean acceptDrop(DragSource paramDragSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, Object paramObject)
  {
    CellLayout localCellLayout = getCurrentDropLayout();
    CellLayout.CellInfo localCellInfo1 = this.mDragInfo;
    int i;
    label20: int j;
    if (localCellInfo1 == null)
    {
      i = 1;
      if (localCellInfo1 != null)
        break label95;
      j = 1;
      if (this.mVacantCache == null)
        label28: if (localCellInfo1 != null)
          break label105;
    }
    for (View localView = null; ; localView = localCellInfo1.cell)
    {
      CellLayout.CellInfo localCellInfo2 = localCellLayout.findAllVacantCells(null, localView);
      this.mVacantCache = localCellInfo2;
      CellLayout.CellInfo localCellInfo3 = this.mVacantCache;
      int[] arrayOfInt = this.mTempEstimate;
      return localCellInfo3.findCellForSpan(arrayOfInt, i, j, 0);
      i = localCellInfo1.spanX;
      break label20:
      label95: j = localCellInfo1.spanY;
      label105: break label28:
    }
  }

  void addApplicationShortcut(ShortcutInfo paramShortcutInfo, CellLayout.CellInfo paramCellInfo)
  {
    addApplicationShortcut(paramShortcutInfo, paramCellInfo, 0);
  }

  void addApplicationShortcut(ShortcutInfo paramShortcutInfo, CellLayout.CellInfo paramCellInfo, boolean paramBoolean)
  {
    int i = paramCellInfo.screen;
    CellLayout localCellLayout = (CellLayout)getChildAt(i);
    int[] arrayOfInt = new int[2];
    int j = paramCellInfo.cellX;
    int k = paramCellInfo.cellY;
    localCellLayout.cellToPoint(j, k, arrayOfInt);
    int l = arrayOfInt[0];
    int i1 = arrayOfInt[1];
    Workspace localWorkspace = this;
    ShortcutInfo localShortcutInfo = paramShortcutInfo;
    boolean bool = paramBoolean;
    localWorkspace.onDropExternal(l, i1, localShortcutInfo, localCellLayout, bool);
  }

  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    Folder localFolder;
    if (!this.mLauncher.isAllAppsVisible())
    {
      localFolder = getOpenFolder();
      if (localFolder != null)
        break label120;
      int i = this.mCurrentScreen;
      getChildAt(i).addFocusables(paramArrayList, paramInt1);
      if (paramInt1 != 17)
        break label71;
      if (this.mCurrentScreen > 0)
      {
        int j = this.mCurrentScreen - 1;
        getChildAt(j).addFocusables(paramArrayList, paramInt1);
      }
    }
    while (true)
    {
      return;
      label71: if (paramInt1 != 66)
        continue;
      int k = this.mCurrentScreen;
      int l = getChildCount() - 1;
      if (k >= l)
        continue;
      int i1 = this.mCurrentScreen + 1;
      getChildAt(i1).addFocusables(paramArrayList, paramInt1);
      continue;
      label120: localFolder.addFocusables(paramArrayList, paramInt1);
    }
  }

  void addInCurrentScreen(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = this.mCurrentScreen;
    Workspace localWorkspace = this;
    View localView = paramView;
    int j = paramInt1;
    int k = paramInt2;
    int l = paramInt3;
    int i1 = paramInt4;
    localWorkspace.addInScreen(localView, i, j, k, l, i1, 0);
  }

  void addInCurrentScreen(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = this.mCurrentScreen;
    Workspace localWorkspace = this;
    View localView = paramView;
    int j = paramInt1;
    int k = paramInt2;
    int l = paramInt3;
    int i1 = paramInt4;
    boolean bool = paramBoolean;
    localWorkspace.addInScreen(localView, i, j, k, l, i1, bool);
  }

  void addInScreen(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    Workspace localWorkspace = this;
    View localView = paramView;
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    int l = paramInt4;
    int i1 = paramInt5;
    localWorkspace.addInScreen(localView, i, j, k, l, i1, 0);
  }

  void addInScreen(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
  {
    if (paramInt1 >= 0)
    {
      int i = getChildCount();
      if (paramInt1 < i)
        break label75;
    }
    StringBuilder localStringBuilder = new StringBuilder().append("The screen must be >= 0 and < ");
    int j = getChildCount();
    String str = j + " (was " + paramInt1 + "); skipping child";
    int k = Log.e("Launcher2.Workspace", str);
    label74: return;
    label75: clearVacantCache();
    CellLayout localCellLayout = (CellLayout)getChildAt(paramInt1);
    CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)paramView.getLayoutParams();
    if (localLayoutParams == null)
    {
      localLayoutParams = new CellLayout.LayoutParams(paramInt2, paramInt3, paramInt4, paramInt5);
      label119: if (!paramBoolean)
        break label244;
    }
    for (int l = 0; ; l = -1)
    {
      localCellLayout.addView(paramView, l, localLayoutParams);
      if (!paramView instanceof Folder)
      {
        paramView.setHapticFeedbackEnabled(0);
        View.OnLongClickListener localOnLongClickListener = this.mLongClickListener;
        paramView.setOnLongClickListener(localOnLongClickListener);
      }
      if (paramView instanceof DragScroller)
      {
        DragController localDragController = this.mDragController;
        DropTarget localDropTarget = (DragScroller)paramView;
        localDragController.addDropTarget(localDropTarget);
      }
      View localView = searchIMTKWidget(paramView);
      if (localView != null);
      ((IMTKWidget)localView).setScreen(paramInt1);
      break label74:
      localLayoutParams.cellX = paramInt2;
      localLayoutParams.cellY = paramInt3;
      localLayoutParams.cellHSpan = paramInt4;
      localLayoutParams.cellVSpan = paramInt5;
      label244: break label119:
    }
  }

  protected CellLayout addScreen(int paramInt)
  {
    int i = 2130903064;
    if (this.mLauncher.isAllAppsShow())
      i = 2130903065;
    CellLayout localCellLayout = (CellLayout)LayoutInflater.from(this.mLauncher).inflate(i, this, 0);
    this.mCurrentScreen = paramInt;
    this.mNextScreen = 64537;
    addView(localCellLayout, paramInt);
    View.OnLongClickListener localOnLongClickListener = this.mLongClickListener;
    localCellLayout.setOnLongClickListener(localOnLongClickListener);
    return localCellLayout;
  }

  public void addView(View paramView)
  {
    if (!paramView instanceof CellLayout)
      throw new IllegalArgumentException("A Workspace can only have CellLayout children.");
    super.addView(paramView);
  }

  public void addView(View paramView, int paramInt)
  {
    if (!paramView instanceof CellLayout)
      throw new IllegalArgumentException("A Workspace can only have CellLayout children.");
    super.addView(paramView, paramInt);
  }

  public void addView(View paramView, int paramInt1, int paramInt2)
  {
    if (!paramView instanceof CellLayout)
      throw new IllegalArgumentException("A Workspace can only have CellLayout children.");
    super.addView(paramView, paramInt1, paramInt2);
  }

  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (!paramView instanceof CellLayout)
      throw new IllegalArgumentException("A Workspace can only have CellLayout children.");
    super.addView(paramView, paramInt, paramLayoutParams);
  }

  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (!paramView instanceof CellLayout)
      throw new IllegalArgumentException("A Workspace can only have CellLayout children.");
    super.addView(paramView, paramLayoutParams);
  }

  public boolean allowLongPress()
  {
    return this.mAllowLongPress;
  }

  void clearChildrenCache()
  {
    int i = getChildCount();
    int j = 0;
    while (j < i)
    {
      ((CellLayout)getChildAt(j)).setChildrenDrawnWithCacheEnabled(0);
      j += 1;
    }
  }

  public void computeScroll()
  {
    if (this.mScroller.computeScrollOffset())
    {
      float f1 = this.mScroller.getCurrX();
      this.mTouchX = f1;
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      scrollTo(i, j);
      updateWallpaperOffset();
      requestCellLayout(1);
    }
    while (true)
    {
      label56: return;
      if (this.mNextScreen != 64537)
      {
        if (this.mNextScreen == -1)
        {
          int k = getChildCount() - 1;
          this.mCurrentScreen = k;
          int l = this.mCurrentScreen;
          int i1 = getWidth();
          int i2 = l * i1;
          int i3 = getScrollY();
          scrollTo(i2, i3);
        }
        while (true)
        {
          this.mNextScreen = 64537;
          break label56:
          int i4 = this.mNextScreen;
          int i5 = getChildCount();
          if (i4 == i5)
          {
            this.mCurrentScreen = 0;
            int i6 = getScrollY();
            scrollTo(0, i6);
          }
          int i7 = this.mNextScreen;
          int i8 = getChildCount() - 1;
          int i9 = Math.min(i7, i8);
          int i10 = Math.max(0, i9);
          this.mCurrentScreen = i10;
        }
      }
      if (this.mTouchState != 1)
        continue;
      float f2 = (float)System.nanoTime() / 1.0E+009F;
      float f3 = this.mSmoothingTime;
      float f4 = f2 - f3;
      float f5 = SMOOTHING_CONSTANT;
      float f6 = (float)Math.exp(f4 / f5);
      float f7 = this.mTouchX;
      float f8 = getScrollX();
      float f9 = f7 - f8;
      float f10 = this.mScrollX;
      float f11 = f9 * f6;
      int i11 = (int)(f10 + f11);
      this.mScrollX = i11;
      this.mSmoothingTime = f2;
      if ((f9 <= 1.0F) && (f9 >= -1.0F))
        continue;
      updateWallpaperOffset();
      requestCellLayout(1);
    }
  }

  protected void controlMTKWidget(int paramInt)
  {
    if ((!canSendMessage) || (this.mDragController.isDraging()))
      return;
    int i = this.mCurrentScreen;
    View localView1 = getChildAt(i);
    View localView2 = searchIMTKWidget(localView1);
    if (localView2 == null)
      return;
    IMTKWidget localIMTKWidget = (IMTKWidget)localView2;
    int j = this.mCurrentScreen;
    boolean bool = localIMTKWidget.moveOut(j);
    canSendMessage = 0;
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    int i = getChildCount();
    int j = this.m_transitionType;
    int k = 1;
    int l = j;
    int i4;
    label74: int i12;
    if ((k != l) && (this.m_transitionType != 0))
    {
      int i1 = i;
      int i2 = 1;
      if (i1 > i2)
      {
        int i3 = getWidth();
        float f1 = this.mScrollX;
        float f2 = i3;
        f3 = f1 / f2;
        i4 = 0;
        int i5 = 0;
        int i6 = i5;
        int i7 = i;
        if (i6 < i7)
        {
          int i8 = this.mScrollX;
          int i9 = i5 * i3;
          int i10 = i8;
          int i11 = i9;
          if (i10 <= i11)
            break label301;
          float f4 = this.mScrollX;
          float f5 = i5 + 1 + 0.01F;
          float f6 = i3;
          float f7 = f5 * f6;
          if (f4 > f7)
            break label301;
          i4 = i5;
        }
        i12 = i4 + 1;
        int i13 = this.mScrollX;
        int i14 = i4 * i3;
        int i15 = i13 - i14;
        if (this.m_isCycle)
        {
          if (f3 >= 0.0F)
            break label310;
          i4 = i - 1;
          i12 = 0;
          i15 = this.mScrollX;
        }
        label213: float f8 = i15;
        float f9 = i3 * 1.0F;
        float f10 = f8 / f9;
        float f11 = 1.0F;
        float f12 = Math.min(f10, f11);
        float f13 = -1.0F;
        float f14 = Math.max(f12, f13);
        switch (this.m_transitionType)
        {
        default:
        case 2:
        case 3:
        case 4:
        case 6:
        case 5:
        }
        while (true)
        {
          label300: return;
          label301: i5 += 1;
          break label74:
          label310: int i16 = i12;
          int i17 = i;
          if (i16 >= i17);
          i4 = i - 1;
          i12 = 0;
          break label213:
          Workspace localWorkspace1 = this;
          Canvas localCanvas1 = paramCanvas;
          int i18 = i4;
          int i19 = i12;
          float f15 = f14;
          localWorkspace1.transitionFade(localCanvas1, i18, i19, f15);
          continue;
          Workspace localWorkspace2 = this;
          Canvas localCanvas2 = paramCanvas;
          int i20 = i4;
          int i21 = i12;
          float f16 = f14;
          localWorkspace2.transitionFlip(localCanvas2, i20, i21, f16);
          continue;
          Workspace localWorkspace3 = this;
          Canvas localCanvas3 = paramCanvas;
          int i22 = i4;
          int i23 = i12;
          float f17 = f14;
          localWorkspace3.transitionCube(localCanvas3, i22, i23, f17);
          continue;
          Workspace localWorkspace4 = this;
          Canvas localCanvas4 = paramCanvas;
          int i24 = i4;
          int i25 = i12;
          float f18 = f14;
          localWorkspace4.transitionPhotowall(localCanvas4, i24, i25, f18);
          continue;
          Workspace localWorkspace5 = this;
          Canvas localCanvas5 = paramCanvas;
          int i26 = i4;
          int i27 = i12;
          float f19 = f14;
          localWorkspace5.transitionWindmill(localCanvas5, i26, i27, f19);
        }
      }
    }
    int i28 = 0;
    int i29 = 0;
    int i30 = this.mTouchState;
    int i31 = 1;
    if (i30 != i31)
    {
      int i32 = this.mNextScreen;
      int i33 = 64537;
      if (i32 != i33);
    }
    for (int i34 = 1; i34 != 0; i34 = 0)
    {
      int i35 = this.mCurrentScreen;
      Workspace localWorkspace6 = this;
      int i36 = i35;
      View localView1 = localWorkspace6.getChildAt(i36);
      if (localView1 != null);
      long l1 = getDrawingTime();
      Workspace localWorkspace7 = this;
      Canvas localCanvas6 = paramCanvas;
      View localView2 = localView1;
      long l2 = l1;
      boolean bool1 = localWorkspace7.drawChild(localCanvas6, localView2, l2);
      break label300:
    }
    long l3 = getDrawingTime();
    int i37 = getWidth();
    float f20 = getScrollX();
    float f21 = i37;
    float f3 = f20 / f21;
    int i38 = 1;
    int i39 = 0;
    int i40 = getChildCount();
    if ((f3 < 0.0F) && (i38 != 0))
    {
      i4 = i40 - 1;
      i12 = 0;
      label686: Workspace localWorkspace8 = this;
      int i41 = i4;
      if (localWorkspace8.isScreenNoValid(i41))
      {
        if ((i12 != 0) || (i39 != 0))
          break label1026;
        int i42 = i40 * i37;
        float f22 = -i42;
        Canvas localCanvas7 = paramCanvas;
        float f23 = f22;
        float f24 = 0.0F;
        localCanvas7.translate(f23, f24);
        Workspace localWorkspace9 = this;
        int i43 = i4;
        View localView3 = localWorkspace9.getChildAt(i43);
        Workspace localWorkspace10 = this;
        Canvas localCanvas8 = paramCanvas;
        View localView4 = localView3;
        long l4 = l3;
        boolean bool2 = localWorkspace10.drawChild(localCanvas8, localView4, l4);
        float f25 = i42;
        Canvas localCanvas9 = paramCanvas;
        float f26 = f25;
        float f27 = 0.0F;
        localCanvas9.translate(f26, f27);
      }
      label814: float f28 = i4;
      if (f3 != f28)
      {
        Workspace localWorkspace11 = this;
        int i44 = i12;
        if (localWorkspace11.isScreenNoValid(i44))
        {
          if ((i38 == 0) || (i12 != 0) || (i39 == 0))
            break label1072;
          int i45 = i40 * i37;
          float f29 = i45;
          Canvas localCanvas10 = paramCanvas;
          float f30 = f29;
          float f31 = 0.0F;
          localCanvas10.translate(f30, f31);
          Workspace localWorkspace12 = this;
          int i46 = i12;
          View localView5 = localWorkspace12.getChildAt(i46);
          Workspace localWorkspace13 = this;
          Canvas localCanvas11 = paramCanvas;
          View localView6 = localView5;
          long l5 = l3;
          boolean bool3 = localWorkspace13.drawChild(localCanvas11, localView6, l5);
          float f32 = -i45;
          Canvas localCanvas12 = paramCanvas;
          float f33 = f32;
          float f34 = 0.0F;
          localCanvas12.translate(f33, f34);
        }
      }
    }
    while (true)
    {
      if (i28 != 0);
      Canvas localCanvas13 = paramCanvas;
      int i47 = i29;
      localCanvas13.restoreToCount(i47);
      break label300:
      int i48 = (int)f3;
      int i49 = i40 - 1;
      i4 = Math.min(i48, i49);
      i12 = i4 + 1;
      if (i38 != 0);
      i12 %= i40;
      i39 = 1;
      break label686:
      label1026: Workspace localWorkspace14 = this;
      int i50 = i4;
      View localView7 = localWorkspace14.getChildAt(i50);
      Workspace localWorkspace15 = this;
      Canvas localCanvas14 = paramCanvas;
      View localView8 = localView7;
      long l6 = l3;
      boolean bool4 = localWorkspace15.drawChild(localCanvas14, localView8, l6);
      break label814:
      label1072: Workspace localWorkspace16 = this;
      int i51 = i12;
      View localView9 = localWorkspace16.getChildAt(i51);
      Workspace localWorkspace17 = this;
      Canvas localCanvas15 = paramCanvas;
      View localView10 = localView9;
      long l7 = l3;
      boolean bool5 = localWorkspace17.drawChild(localCanvas15, localView10, l7);
    }
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mLauncher.isDialogShow());
    boolean bool;
    for (int i = 0; ; bool = super.dispatchTouchEvent(paramMotionEvent))
      while (true)
      {
        return i;
        if (paramMotionEvent.getAction() != 0)
          break;
        if (this.mLauncher.isWorkspaceLocked())
          i = 0;
        if ((!this.mLauncher.isAllAppsVisible()) || ((this.mLauncher.isAllAppsLoaded()) && (this.isAllAppsLoaded)))
          break;
        i = 0;
      }
  }

  public boolean dispatchUnhandledMove(View paramView, int paramInt)
  {
    if (paramInt == 17)
    {
      if (getCurrentScreen() <= 0)
        break label80;
      int i = getCurrentScreen() - 1;
      snapToScreen(i);
    }
    label80: boolean bool;
    for (int j = 1; ; bool = super.dispatchUnhandledMove(paramView, paramInt))
      while (true)
      {
        return j;
        if (paramInt != 66)
          break;
        int k = getCurrentScreen();
        int l = getChildCount() - 1;
        if (k >= l)
          break;
        int i1 = getCurrentScreen() + 1;
        snapToScreen(i1);
        j = 1;
      }
  }

  void enableChildrenCache(int paramInt1, int paramInt2)
  {
    if (paramInt1 > paramInt2)
    {
      int i = paramInt1;
      paramInt1 = paramInt2;
      paramInt2 = i;
    }
    int j = getChildCount();
    int k = Math.max(paramInt1, 0);
    int l = j - 1;
    paramInt2 = Math.min(paramInt2, l);
    int i1 = k;
    while (i1 <= paramInt2)
    {
      CellLayout localCellLayout = (CellLayout)getChildAt(i1);
      localCellLayout.setChildrenDrawnWithCacheEnabled(1);
      localCellLayout.setChildrenDrawingCacheEnabled(1);
      i1 += 1;
    }
  }

  public Rect estimateDropLocation(DragSource paramDragSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, Object paramObject, Rect paramRect)
  {
    CellLayout localCellLayout = getCurrentDropLayout();
    CellLayout.CellInfo localCellInfo = this.mDragInfo;
    int i;
    label20: int j;
    label28: View localView;
    label36: Rect localRect1;
    label45: int[] arrayOfInt2;
    if (localCellInfo == null)
    {
      i = 1;
      if (localCellInfo != null)
        break label104;
      j = 1;
      if (localCellInfo != null)
        break label114;
      localView = null;
      if (paramRect == null)
        break label124;
      localRect1 = paramRect;
      int k = paramInt1 - paramInt3;
      int l = paramInt2 - paramInt4;
      int[] arrayOfInt1 = this.mTempCell;
      arrayOfInt2 = estimateDropCell(k, l, i, j, localView, localCellLayout, arrayOfInt1);
      if (arrayOfInt2 != null)
        break label136;
    }
    for (Rect localRect2 = null; ; localRect2 = localRect1)
    {
      return localRect2;
      i = localCellInfo.spanX;
      break label20:
      label104: j = localCellInfo.spanY;
      break label28:
      label114: localView = localCellInfo.cell;
      break label36:
      label124: localRect1 = new Rect();
      break label45:
      label136: int i1 = arrayOfInt2[0];
      int i2 = arrayOfInt2[1];
      int[] arrayOfInt3 = this.mTempEstimate;
      localCellLayout.cellToPoint(i1, i2, arrayOfInt3);
      int i3 = this.mTempEstimate[0];
      localRect1.left = i3;
      int i4 = this.mTempEstimate[1];
      localRect1.top = i4;
      int i5 = arrayOfInt2[0] + i;
      int i6 = arrayOfInt2[1] + j;
      int[] arrayOfInt4 = this.mTempEstimate;
      localCellLayout.cellToPoint(i5, i6, arrayOfInt4);
      int i7 = this.mTempEstimate[0];
      localRect1.right = i7;
      int i8 = this.mTempEstimate[1];
      localRect1.bottom = i8;
    }
  }

  CellLayout.CellInfo findAllVacantCells(boolean[] paramArrayOfBoolean)
  {
    int i = this.mCurrentScreen;
    CellLayout localCellLayout = (CellLayout)getChildAt(i);
    if (localCellLayout != null);
    for (CellLayout.CellInfo localCellInfo = localCellLayout.findAllVacantCells(paramArrayOfBoolean, null); ; localCellInfo = null)
      return localCellInfo;
  }

  public void focusableViewAvailable(View paramView)
  {
    int i = this.mCurrentScreen;
    View localView1 = getChildAt(i);
    for (View localView2 = paramView; ; localView2 = (View)localView2.getParent())
    {
      if (localView2 == localView1)
        super.focusableViewAvailable(paramView);
      do
        return;
      while ((localView2 == this) || (!localView2.getParent() instanceof View));
    }
  }

  public int getCurScreen()
  {
    return this.mCurrentScreen;
  }

  int getCurrentScreen()
  {
    return this.mCurrentScreen;
  }

  public void getDefaultScreenPrefs(String paramString)
  {
    int i = this.mContext.getSharedPreferences("DefaultScreen", 0).getInt(paramString, 0);
    this.mDefaultScreen = i;
  }

  public Folder getFolderForTag(Object paramObject)
  {
    int i = getChildCount();
    int j = 0;
    label7: int l;
    label32: Folder localFolder1;
    if (j < i)
    {
      CellLayout localCellLayout = (CellLayout)getChildAt(j);
      int k = localCellLayout.getChildCount();
      l = 0;
      if (l < k)
      {
        View localView = localCellLayout.getChildAt(l);
        CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)localView.getLayoutParams();
        if ((localLayoutParams.cellHSpan == 4) && (localLayoutParams.cellVSpan == 4) && (localView instanceof Folder))
        {
          localFolder1 = (Folder)localView;
          if (localFolder1.getInfo() != paramObject);
        }
      }
    }
    for (Folder localFolder2 = localFolder1; ; localFolder2 = null)
    {
      return localFolder2;
      l += 1;
      break label32:
      j += 1;
      break label7:
    }
  }

  public void getIsScreenCountChangedPrefs(String paramString)
  {
    boolean bool = this.mContext.getSharedPreferences("IsScreenChanged", 0).getBoolean(paramString, 0);
    this.mIsScreenCountChanged = bool;
  }

  Folder getOpenFolder()
  {
    int i = this.mCurrentScreen;
    CellLayout localCellLayout = (CellLayout)getChildAt(i);
    if (localCellLayout == null);
    for (Folder localFolder = null; ; localFolder = null)
    {
      return localFolder;
      int j = localCellLayout.getChildCount();
      int k = 0;
      while (k < j)
      {
        View localView = localCellLayout.getChildAt(k);
        CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)localView.getLayoutParams();
        if ((localLayoutParams.cellHSpan == 4) && (localLayoutParams.cellVSpan == 4) && (localView instanceof Folder))
          localFolder = (Folder)localView;
        k += 1;
      }
    }
  }

  ArrayList<Folder> getOpenFolders()
  {
    int i = getChildCount();
    ArrayList localArrayList = new ArrayList(i);
    int j = 0;
    if (j < i)
    {
      label16: CellLayout localCellLayout = (CellLayout)getChildAt(j);
      int k = localCellLayout.getChildCount();
      int l = 0;
      while (true)
      {
        if (l < k)
        {
          View localView = localCellLayout.getChildAt(l);
          CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)localView.getLayoutParams();
          if ((localLayoutParams.cellHSpan != 4) || (localLayoutParams.cellVSpan != 4) || (!localView instanceof Folder))
            break label115;
          Folder localFolder = (Folder)localView;
          boolean bool = localArrayList.add(localFolder);
        }
        j += 1;
        break label16:
        label115: l += 1;
      }
    }
    return localArrayList;
  }

  public int getScreenForView(View paramView)
  {
    int j;
    if (paramView != null)
    {
      ViewParent localViewParent = paramView.getParent();
      int i = getChildCount();
      j = 0;
      if (j < i)
      {
        label17: View localView = getChildAt(j);
        if (localViewParent != localView);
      }
    }
    for (int k = j; ; k = -1)
    {
      return k;
      j += 1;
      break label17:
    }
  }

  public View getViewForTag(Object paramObject)
  {
    int i = getChildCount();
    int j = 0;
    label7: int l;
    label32: View localView1;
    if (j < i)
    {
      CellLayout localCellLayout = (CellLayout)getChildAt(j);
      int k = localCellLayout.getChildCount();
      l = 0;
      if (l < k)
      {
        localView1 = localCellLayout.getChildAt(l);
        if (localView1.getTag() != paramObject);
      }
    }
    for (View localView2 = localView1; ; localView2 = null)
    {
      return localView2;
      l += 1;
      break label32:
      j += 1;
      break label7:
    }
  }

  public void hideWallpaper(boolean paramBoolean)
  {
    IBinder localIBinder;
    if ((this.mWallpaperOverflow) && (this.mNeedWallpaper))
    {
      localIBinder = getWindowToken();
      if (localIBinder != null)
      {
        if (!paramBoolean)
          break label53;
        WallpaperManager localWallpaperManager1 = this.mWallpaperManager;
        int i = 0;
        int j = 0;
        localWallpaperManager1.sendWallpaperCommand(localIBinder, "hide", 0, i, j, null);
      }
    }
    while (true)
    {
      return;
      label53: WallpaperManager localWallpaperManager2 = this.mWallpaperManager;
      int k = 0;
      int l = 0;
      localWallpaperManager2.sendWallpaperCommand(localIBinder, "show", 0, k, l, null);
    }
  }

  boolean isDefaultScreenShowing()
  {
    int i = this.mCurrentScreen;
    int j = this.mDefaultScreen;
    if (i == j);
    for (int k = 1; ; k = 0)
      return k;
  }

  void moveToDefaultScreen(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      int i = this.mDefaultScreen;
      snapToScreen(i);
    }
    while (true)
    {
      int j = this.mDefaultScreen;
      boolean bool = getChildAt(j).requestFocus();
      return;
      int k = this.mDefaultScreen;
      setCurrentScreen(k);
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    computeScroll();
    if (this.mDragController == null)
      return;
    DragController localDragController = this.mDragController;
    IBinder localIBinder = getWindowToken();
    localDragController.setWindowToken(localIBinder);
  }

  public void onDragEnter(DragSource paramDragSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, Object paramObject)
  {
    clearVacantCache();
    this.mLauncher.closeAllFolder();
    changeCellLayoutBackground(1);
  }

  public void onDragExit(DragSource paramDragSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, Object paramObject)
  {
    clearVacantCache();
    changeCellLayoutBackground(0);
  }

  public void onDragOver(DragSource paramDragSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, Object paramObject)
  {
  }

  public void onDrop(DragSource paramDragSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, DragView paramDragView, Object paramObject)
  {
    CellLayout localCellLayout1 = getCurrentDropLayout();
    DragSource localDragSource = paramDragSource;
    Workspace localWorkspace1 = this;
    if (localDragSource != localWorkspace1)
    {
      int i = paramInt1 - paramInt3;
      int j = paramInt2 - paramInt4;
      Workspace localWorkspace2 = this;
      int k = i;
      int l = j;
      Object localObject = paramObject;
      CellLayout localCellLayout2 = localCellLayout1;
      localWorkspace2.onDropExternal(k, l, localObject, localCellLayout2);
    }
    do
    {
      label63: Workspace localWorkspace3 = this;
      int i1 = 0;
      localWorkspace3.changeCellLayoutBackground(i1);
      return;
    }
    while (this.mDragInfo == null);
    View localView1 = this.mDragInfo.cell;
    if (this.mScroller.isFinished());
    for (int i2 = this.mCurrentScreen; ; i2 = this.mNextScreen)
    {
      int i3 = this.mDragInfo.screen;
      int i4 = i2;
      int i5 = i3;
      if (i4 != i5)
      {
        int i6 = this.mDragInfo.screen;
        Workspace localWorkspace4 = this;
        int i7 = i6;
        CellLayout localCellLayout3 = (CellLayout)localWorkspace4.getChildAt(i7);
        View localView2 = localView1;
        localCellLayout3.removeView(localView2);
        localCellLayout1.addView(localView1);
      }
      int i8 = paramInt1 - paramInt3;
      int i9 = paramInt2 - paramInt4;
      int i10 = this.mDragInfo.spanX;
      int i11 = this.mDragInfo.spanY;
      int[] arrayOfInt1 = this.mTargetCell;
      int[] arrayOfInt2 = estimateDropCell(i8, i9, i10, i11, localView1, localCellLayout1, arrayOfInt1);
      this.mTargetCell = arrayOfInt2;
      int[] arrayOfInt3 = this.mTargetCell;
      localCellLayout1.onDropChild(localView1, arrayOfInt3);
      Workspace localWorkspace5 = this;
      View localView3 = localView1;
      View localView4 = localWorkspace5.searchIMTKWidget(localView3);
      if (localView4 != null)
        ((IMTKWidget)localView4).stopDrag();
      ItemInfo localItemInfo = (ItemInfo)localView1.getTag();
      CellLayout.LayoutParams localLayoutParams = (CellLayout.LayoutParams)localView1.getLayoutParams();
      Launcher localLauncher = this.mLauncher;
      int i12 = localLayoutParams.cellX;
      int i13 = localLayoutParams.cellY;
      LauncherModel.moveItemInDatabase(localLauncher, localItemInfo, 65436L, i2, i12, i13);
      break label63:
    }
  }

  public void onDropCompleted(View paramView, boolean paramBoolean)
  {
    clearVacantCache();
    if (paramBoolean)
      if ((paramView != this) && (this.mDragInfo != null))
      {
        int i = this.mDragInfo.screen;
        CellLayout localCellLayout1 = (CellLayout)getChildAt(i);
        View localView1 = this.mDragInfo.cell;
        localCellLayout1.removeView(localView1);
        if (this.mDragInfo.cell instanceof DragScroller)
        {
          DragController localDragController = this.mDragController;
          DropTarget localDropTarget = (DragScroller)this.mDragInfo.cell;
          localDragController.removeDropTarget(localDropTarget);
        }
      }
    while (true)
    {
      this.mDragInfo = null;
      return;
      if (this.mDragInfo == null)
        continue;
      int j = this.mDragInfo.screen;
      CellLayout localCellLayout2 = (CellLayout)getChildAt(j);
      View localView2 = this.mDragInfo.cell;
      localCellLayout2.onDropAborted(localView2);
    }
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = this.mLauncher.isAllAppsVisible();
    int i = paramMotionEvent.getAction();
    if ((i == 2) && (this.mTouchState != 0));
    for (int j = 1; ; j = 0)
    {
      label28: return j;
      acquireVelocityTrackerAndAddMovement(paramMotionEvent);
      switch (i & 0xFF)
      {
      case 4:
      case 5:
      default:
      case 2:
      case 0:
      case 1:
      case 3:
      case 6:
      }
      while (this.mTouchState != 0)
      {
        j = 1;
        break label28:
        int k = this.mActivePointerId;
        MotionEvent localMotionEvent1 = paramMotionEvent;
        int l = k;
        int i1 = localMotionEvent1.findPointerIndex(l);
        MotionEvent localMotionEvent2 = paramMotionEvent;
        int i2 = i1;
        float f1 = localMotionEvent2.getX(i2);
        MotionEvent localMotionEvent3 = paramMotionEvent;
        int i3 = i1;
        float f2 = localMotionEvent3.getY(i3);
        float f3 = this.mLastMotionX;
        int i4 = (int)Math.abs(f1 - f3);
        float f4 = this.mLastMotionX;
        int i5 = (int)Math.abs(f2 - f4);
        int i6 = this.mTouchSlop;
        int i7 = i4;
        int i8 = i6;
        if (i7 <= i8)
          continue;
        int i9 = 1;
        this.mTouchState = i9;
        float f5 = f1;
        this.mLastMotionX = f5;
        float f6 = this.mScrollX;
        this.mTouchX = f6;
        float f7 = (float)System.nanoTime() / 1.0E+009F;
        this.mSmoothingTime = f7;
        int i10 = this.mCurrentScreen - 1;
        int i11 = this.mCurrentScreen + 1;
        Workspace localWorkspace1 = this;
        int i12 = i10;
        int i13 = i11;
        localWorkspace1.enableChildrenCache(i12, i13);
        if (!this.mAllowLongPress)
          continue;
        int i14 = 0;
        this.mAllowLongPress = i14;
        int i15 = this.mCurrentScreen;
        Workspace localWorkspace2 = this;
        int i16 = i15;
        View localView = localWorkspace2.getChildAt(i16);
        if (localView == null)
          continue;
        localView.cancelLongPress();
        continue;
        float f8 = paramMotionEvent.getX();
        float f9 = paramMotionEvent.getY();
        float f10 = f8;
        this.mLastMotionX = f10;
        float f11 = f9;
        this.mLastMotionY = f11;
        MotionEvent localMotionEvent4 = paramMotionEvent;
        int i17 = 0;
        int i18 = localMotionEvent4.getPointerId(i17);
        this.mActivePointerId = i18;
        int i19 = 1;
        this.mAllowLongPress = i19;
        if (this.mScroller.isFinished());
        for (j = 0; ; j = 1)
        {
          int i20 = j;
          this.mTouchState = i20;
          if (this.m_savedTransitionType == 0);
          int i21 = (int)(Math.random() * 7.0D);
          int i22;
          for (this.m_transitionType = i21; ; this.m_transitionType = i22)
          {
            if (this.m_transitionType == 0);
            i22 = (int)(Math.random() * 7.0D);
          }
        }
        if (this.mTouchState != 1)
        {
          int i23 = this.mCurrentScreen;
          Workspace localWorkspace3 = this;
          int i24 = i23;
          if (!((CellLayout)localWorkspace3.getChildAt(i24)).lastDownOnOccupiedCell())
          {
            int[] arrayOfInt1 = this.mTempCell;
            Workspace localWorkspace4 = this;
            int[] arrayOfInt2 = arrayOfInt1;
            localWorkspace4.getLocationOnScreen(arrayOfInt2);
            int i25 = this.mActivePointerId;
            MotionEvent localMotionEvent5 = paramMotionEvent;
            int i26 = i25;
            int i27 = localMotionEvent5.findPointerIndex(i26);
            WallpaperManager localWallpaperManager = this.mWallpaperManager;
            IBinder localIBinder = getWindowToken();
            int i28 = this.mTempCell[0];
            MotionEvent localMotionEvent6 = paramMotionEvent;
            int i29 = i27;
            int i30 = (int)localMotionEvent6.getX(i29) + i28;
            int i31 = this.mTempCell[1];
            MotionEvent localMotionEvent7 = paramMotionEvent;
            int i32 = i27;
            int i33 = (int)localMotionEvent7.getY(i32) + i31;
            localWallpaperManager.sendWallpaperCommand(localIBinder, "android.wallpaper.tap", i30, i33, 0, null);
          }
        }
        clearChildrenCache();
        int i34 = 0;
        this.mTouchState = i34;
        int i35 = -1;
        this.mActivePointerId = i35;
        int i36 = 0;
        this.mAllowLongPress = i36;
        releaseVelocityTracker();
        continue;
        onSecondaryPointerUp(paramMotionEvent);
      }
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getChildCount();
    int j = 0;
    while (j < i)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() != 8)
      {
        int k = localView.getMeasuredWidth();
        int l = 0 + k;
        int i1 = localView.getMeasuredHeight();
        localView.layout(0, 0, l, i1);
        int i2 = 0 + k;
      }
      j += 1;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getSize(paramInt1);
    if (View.MeasureSpec.getMode(paramInt1) != 1073741824)
      throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
    if (View.MeasureSpec.getMode(paramInt2) != 1073741824)
      throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
    int j = getChildCount();
    int k = 0;
    while (k < j)
    {
      super.getChildAt(k).measure(paramInt1, paramInt2);
      View localView = super.getChildAt(k);
      View.OnLongClickListener localOnLongClickListener = this.mLongClickListener;
      localView.setOnLongClickListener(localOnLongClickListener);
      k += 1;
    }
    if (!this.mFirstLayout)
      return;
    setHorizontalScrollBarEnabled(0);
    int l = this.mCurrentScreen * i;
    scrollTo(l, 0);
    setHorizontalScrollBarEnabled(1);
    int i1 = (getChildCount() - 1) * i;
    updateWallpaperOffset(i1);
    this.mFirstLayout = 0;
  }

  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    boolean bool1;
    label27: int k;
    int l;
    if (!this.mLauncher.isAllAppsVisible())
    {
      Folder localFolder = getOpenFolder();
      if (localFolder != null)
      {
        bool1 = localFolder.requestFocus(paramInt, paramRect);
        return bool1;
      }
      if (this.mNextScreen == 64537)
        break label99;
      int i = this.mNextScreen;
      int j = getChildCount();
      k = i + j;
      l = getChildCount();
    }
    for (int i1 = k % l; ; i1 = this.mCurrentScreen)
    {
      if (getChildAt(i1) != null)
        boolean bool2 = getChildAt(i1).requestFocus(paramInt, paramRect);
      bool1 = false;
      label99: break label27:
    }
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    if (localSavedState.currentScreen == -1)
      return;
    int i = localSavedState.currentScreen;
    this.mCurrentScreen = i;
    Launcher.setScreen(this.mCurrentScreen);
  }

  protected Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState();
    int i = this.mCurrentScreen;
    localSavedState.currentScreen = i;
    return localSavedState;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    acquireVelocityTrackerAndAddMovement(paramMotionEvent);
    switch (paramMotionEvent.getAction() & 0xFF)
    {
    case 4:
    case 5:
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    case 6:
    }
    while (true)
    {
      label56: return true;
      if (!this.mScroller.isFinished())
        this.mScroller.abortAnimation();
      float f1 = paramMotionEvent.getX();
      this.mLastMotionX = f1;
      MotionEvent localMotionEvent1 = paramMotionEvent;
      int i = 0;
      int j = localMotionEvent1.getPointerId(i);
      this.mActivePointerId = j;
      if (this.mTouchState != 1)
        continue;
      int k = this.mCurrentScreen - 1;
      int l = this.mCurrentScreen + 1;
      Workspace localWorkspace1 = this;
      int i1 = k;
      int i2 = l;
      localWorkspace1.enableChildrenCache(i1, i2);
      continue;
      this.mPageIndicator.showScroller();
      int i3 = this.mActivePointerId;
      MotionEvent localMotionEvent2 = paramMotionEvent;
      int i4 = i3;
      int i5 = localMotionEvent2.findPointerIndex(i4);
      MotionEvent localMotionEvent3 = paramMotionEvent;
      int i6 = i5;
      float f2 = localMotionEvent3.getX(i6);
      int i7 = (int)(this.mLastMotionX - f2);
      float f3 = f2;
      this.mLastMotionX = f3;
      if (this.mTouchState != 1)
        continue;
      if (i7 < 0)
      {
        float f9;
        if (this.mTouchX > 0.0F)
        {
          float f4 = this.mTouchX;
          float f5 = -this.mTouchX;
          float f6 = i7;
          float f7 = Math.max(f5, f6);
          float f8 = f4 + f7;
          this.mTouchX = f8;
          f9 = (float)System.nanoTime() / 1.0E+009F;
        }
        float f15;
        for (this.mSmoothingTime = f9; ; this.mSmoothingTime = f15)
        {
          float f10;
          float f11;
          do
          {
            int i8 = (int)this.mTouchX;
            this.mScrollX = i8;
            int i9 = 0;
            this.mNeedWallpaperRoll = i9;
            updateWallpaperOffset();
            Workspace localWorkspace2 = this;
            int i10 = 0;
            localWorkspace2.requestCellLayout(i10);
            PageIndicatorView localPageIndicatorView1 = this.mPageIndicator;
            int i11 = (int)this.mTouchX;
            localPageIndicatorView1.scrollX(i11);
            break label56:
            f10 = this.mTouchX;
            f11 = -getWidth();
          }
          while (f10 <= f11);
          float f12 = this.mTouchX;
          float f13 = i7;
          float f14 = f12 + f13;
          this.mTouchX = f14;
          f15 = (float)System.nanoTime() / 1.0E+009F;
        }
      }
      if (i7 > 0)
      {
        int i12 = getChildCount() - 1;
        Workspace localWorkspace3 = this;
        int i13 = i12;
        float f16 = localWorkspace3.getChildAt(i13).getRight();
        float f17 = this.mTouchX;
        float f18 = f16 - f17 - 0.0F;
        if (f18 > 0.0F)
        {
          float f19 = this.mTouchX;
          float f20 = i7;
          float f21 = Math.min(f18, f20);
          float f22 = f19 + f21;
          this.mTouchX = f22;
          float f23 = (float)System.nanoTime() / 1.0E+009F;
          this.mSmoothingTime = f23;
        }
        int i14 = (int)this.mTouchX;
        this.mScrollX = i14;
        int i15 = 0;
        this.mNeedWallpaperRoll = i15;
        updateWallpaperOffset();
        Workspace localWorkspace4 = this;
        int i16 = 0;
        localWorkspace4.requestCellLayout(i16);
        PageIndicatorView localPageIndicatorView2 = this.mPageIndicator;
        int i17 = (int)this.mTouchX;
        localPageIndicatorView2.scrollX(i17);
      }
      boolean bool = awakenScrollBars();
      continue;
      int i19;
      int i21;
      float f27;
      int i22;
      if (this.mTouchState == 1)
      {
        VelocityTracker localVelocityTracker = this.mVelocityTracker;
        float f24 = this.mMaximumVelocity;
        localVelocityTracker.computeCurrentVelocity(1000, f24);
        int i18 = this.mActivePointerId;
        i19 = (int)localVelocityTracker.getXVelocity(i18);
        int i20 = getWidth();
        double d1 = getScrollX();
        double d2 = i20 / 2.0D;
        double d3 = d1 + d2;
        double d4 = i20;
        i21 = (int)Math.floor(d3 / d4);
        float f25 = getScrollX();
        float f26 = i20;
        f27 = f25 / f26;
        if ((i19 <= 600) || (this.mCurrentScreen <= -1))
          break label836;
        float f28 = i21;
        if (f27 >= f28)
          break label827;
        i22 = this.mCurrentScreen - 1;
        label749: int i23 = Math.min(i21, i22);
        Workspace localWorkspace5 = this;
        int i24 = i23;
        int i25 = i19;
        int i26 = 1;
        localWorkspace5.snapToScreen(i24, i25, i26);
      }
      while (true)
      {
        label784: int i27 = 0;
        this.mScrollSpeedKeep = i27;
        int i28 = 0;
        this.mTouchState = i28;
        int i29 = -1;
        this.mActivePointerId = i29;
        releaseVelocityTracker();
        this.mPageIndicator.hideScroller();
        break label56:
        label827: i22 = this.mCurrentScreen;
        break label749:
        if (i19 < 64936)
        {
          label836: int i30 = this.mCurrentScreen;
          int i31 = getChildCount() - 0;
          if (i30 < i31)
          {
            float f29 = i21;
            if (f27 > f29);
            for (i22 = this.mCurrentScreen + 1; ; i22 = this.mCurrentScreen)
            {
              int i32 = Math.max(i21, i22);
              Workspace localWorkspace6 = this;
              int i33 = i32;
              int i34 = i19;
              int i35 = 1;
              localWorkspace6.snapToScreen(i33, i34, i35);
              break label784:
            }
          }
        }
        Workspace localWorkspace7 = this;
        int i36 = i21;
        int i37 = 0;
        int i38 = 1;
        localWorkspace7.snapToScreen(i36, i37, i38);
      }
      this.mPageIndicator.hideScroller();
      if (this.mTouchState == 1)
      {
        int i39 = getWidth();
        int i40 = getScrollX();
        int i41 = i39 / 2;
        int i42 = (i40 + i41) / i39;
        Workspace localWorkspace8 = this;
        int i43 = i42;
        int i44 = 0;
        int i45 = 1;
        localWorkspace8.snapToScreen(i43, i44, i45);
      }
      int i46 = 0;
      this.mTouchState = i46;
      int i47 = -1;
      this.mActivePointerId = i47;
      releaseVelocityTracker();
      continue;
      onSecondaryPointerUp(paramMotionEvent);
    }
  }

  void removeItems(ArrayList<ApplicationInfo> paramArrayList)
  {
    if (paramArrayList == null)
      return;
    int i = getChildCount();
    PackageManager localPackageManager = getContext().getPackageManager();
    AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(getContext());
    HashSet localHashSet = new HashSet();
    int j = paramArrayList.size();
    int k = 0;
    while (k < j)
    {
      String str = ((ApplicationInfo)paramArrayList.get(k)).componentName.getPackageName();
      boolean bool1 = localHashSet.add(str);
      k += 1;
    }
    k = 0;
    while (true)
    {
      if (k < i);
      CellLayout localCellLayout = (CellLayout)getChildAt(k);
      Workspace.1 local1 = new Workspace.1(this, localCellLayout, localHashSet, localPackageManager);
      boolean bool2 = post(local1);
      k += 1;
    }
  }

  protected void removeScreen(int paramInt)
  {
    CellLayout localCellLayout1 = (CellLayout)getChildAt(paramInt);
    int i = localCellLayout1.getChildCount() - 1;
    ItemInfo localItemInfo;
    while (true)
    {
      if (i < 0)
        break label222;
      localItemInfo = (ItemInfo)localCellLayout1.getChildAt(i).getTag();
      if (localItemInfo != null)
        break;
      label38: i += -1;
    }
    if (localItemInfo.container == 65535L)
      label58: return;
    if ((localItemInfo.container == 65436L) && (localItemInfo instanceof LauncherAppWidgetInfo))
    {
      Launcher localLauncher1 = this.mLauncher;
      LauncherAppWidgetInfo localLauncherAppWidgetInfo1 = (LauncherAppWidgetInfo)localItemInfo;
      localLauncher1.removeAppWidget(localLauncherAppWidgetInfo1);
    }
    if (localItemInfo instanceof UserFolderInfo)
    {
      UserFolderInfo localUserFolderInfo = (UserFolderInfo)localItemInfo;
      LauncherModel.deleteUserFolderContentsFromDatabase(this.mLauncher, localUserFolderInfo);
      this.mLauncher.removeFolder(localUserFolderInfo);
    }
    while (true)
    {
      LauncherModel.deleteItemFromDatabase(this.mLauncher, localItemInfo);
      localCellLayout1.removeViewAt(i);
      break label38:
      if (localItemInfo instanceof FolderInfo)
      {
        FolderInfo localFolderInfo = (FolderInfo)localItemInfo;
        this.mLauncher.removeFolder(localFolderInfo);
      }
      if (!localItemInfo instanceof LauncherAppWidgetInfo)
        continue;
      LauncherAppWidgetInfo localLauncherAppWidgetInfo2 = (LauncherAppWidgetInfo)localItemInfo;
      LauncherAppWidgetHost localLauncherAppWidgetHost = this.mLauncher.getAppWidgetHost();
      if (localLauncherAppWidgetHost == null)
        continue;
      int j = localLauncherAppWidgetInfo2.appWidgetId;
      localLauncherAppWidgetHost.deleteAppWidgetId(j);
    }
    label222: int k = getChildCount();
    int l = paramInt + 1;
    while (l < k)
    {
      Workspace localWorkspace1 = this;
      int i1 = l;
      CellLayout localCellLayout2 = (CellLayout)localWorkspace1.getChildAt(i1);
      CellLayout.CellInfo localCellInfo = localCellLayout2.mCellInfo;
      int i2 = l - 1;
      localCellInfo.screen = i2;
      int i3 = localCellLayout2.getChildCount();
      i = 0;
      while (i < i3)
      {
        localItemInfo = (ItemInfo)localCellLayout2.getChildAt(i).getTag();
        if (localItemInfo.container != 65535L);
        Launcher localLauncher2 = this.mLauncher;
        int i4 = l - 1;
        LauncherModel.moveItemInDatabase(localLauncher2, localItemInfo, i4);
        i += 1;
      }
      l += 1;
    }
    localCellLayout1.removeAllViews();
    int i6;
    if (paramInt == 0)
    {
      int i5 = 0;
      this.mCurrentScreen = i5;
      i6 = 64537;
    }
    int i8;
    for (this.mNextScreen = i6; ; this.mNextScreen = i8)
    {
      View localView1 = getChildAt(paramInt);
      Workspace localWorkspace2 = this;
      View localView2 = localView1;
      localWorkspace2.removeView(localView2);
      break label58:
      int i7 = paramInt - 1;
      this.mCurrentScreen = i7;
      i8 = 64537;
    }
  }

  public void requestCellLayout(boolean paramBoolean)
  {
    int i = this.mCurrentScreen;
    View localView = getChildAt(i);
    if (localView == null)
      invalidate();
    while (true)
    {
      return;
      int j = localView.getWidth();
      if (!paramBoolean)
      {
        int k = this.mScrollX;
        int l = this.mScrollX + j;
        int i1 = localView.getBottom() - 0;
        invalidate(k, 0, l, i1);
      }
      int i2 = this.mScrollX;
      int i3 = this.mScrollX + j;
      int i4 = localView.getBottom() - 0;
      postInvalidate(i2, 0, i3, i4);
    }
  }

  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean)
  {
    int i = indexOfChild(paramView);
    int j = this.mCurrentScreen;
    if ((i != j) || (!this.mScroller.isFinished()))
      if (!this.mLauncher.isWorkspaceLocked())
        snapToScreen(i);
    for (int k = 1; ; k = 0)
      return k;
  }

  public void scrollLeft()
  {
    clearVacantCache();
    if (this.mScroller.isFinished())
      if (this.mCurrentScreen > -1)
      {
        int i = this.mCurrentScreen - 1;
        snapToScreen(i);
      }
    while (true)
    {
      return;
      if (this.mNextScreen <= -1)
        continue;
      int j = this.mNextScreen - 1;
      snapToScreen(j);
    }
  }

  public void scrollRight()
  {
    clearVacantCache();
    if (this.mScroller.isFinished())
    {
      int i = this.mCurrentScreen;
      int j = getChildCount() - 0;
      if (i < j)
      {
        int k = this.mCurrentScreen + 1;
        snapToScreen(k);
      }
    }
    while (true)
    {
      return;
      int l = this.mNextScreen;
      int i1 = getChildCount() - 0;
      if (l >= i1)
        continue;
      int i2 = this.mNextScreen + 1;
      snapToScreen(i2);
    }
  }

  public void scrollTo(int paramInt1, int paramInt2)
  {
    super.scrollTo(paramInt1, paramInt2);
    float f1 = paramInt1;
    this.mTouchX = f1;
    float f2 = (float)System.nanoTime() / 1.0E+009F;
    this.mSmoothingTime = f2;
  }

  public View searchIMTKWidget(View paramView)
  {
    if (paramView instanceof IMTKWidget);
    for (Object localObject = paramView; ; localObject = null)
      while (true)
      {
        return localObject;
        if (!paramView instanceof ViewGroup)
          break;
        int i = ((ViewGroup)paramView).getChildCount();
        int j = 0;
        while (j < i)
        {
          View localView1 = ((ViewGroup)paramView).getChildAt(j);
          View localView2 = searchIMTKWidget(localView1);
          if (localView2 != null)
            localObject = localView2;
          j += 1;
        }
        localObject = null;
      }
  }

  public void setAllowLongPress(boolean paramBoolean)
  {
    this.mAllowLongPress = paramBoolean;
  }

  void setCurrentScreen(int paramInt)
  {
    if (!this.mScroller.isFinished())
      this.mScroller.abortAnimation();
    clearVacantCache();
    int i = getChildCount() - 1;
    int j = Math.min(paramInt, i);
    int k = Math.max(0, j);
    this.mCurrentScreen = k;
    int l = this.mCurrentScreen;
    int i1 = getWidth();
    int i2 = l * i1;
    scrollTo(i2, 0);
    updateWallpaperOffset();
    invalidate();
  }

  void setDefaultScreenDynamic()
  {
    int i = (getChildCount() - 1) / 2;
    this.mDefaultScreen = i;
    int j = this.mDefaultScreen;
    setDefaultScreenPrefs("DefaultScreen", j);
    this.mIsScreenCountChanged = 1;
    boolean bool = this.mIsScreenCountChanged;
    setIsScreenCountChangedPrefs("IsScreenChanged", bool);
  }

  public void setDefaultScreenPrefs(String paramString, int paramInt)
  {
    SharedPreferences.Editor localEditor1 = this.mContext.getSharedPreferences("DefaultScreen", 0).edit();
    SharedPreferences.Editor localEditor2 = localEditor1.putInt(paramString, paramInt);
    boolean bool = localEditor1.commit();
  }

  public void setDragController(DragController paramDragController)
  {
    this.mDragController = paramDragController;
  }

  public void setIsScreenCountChangedPrefs(String paramString, boolean paramBoolean)
  {
    SharedPreferences.Editor localEditor1 = this.mContext.getSharedPreferences("IsScreenChanged", 0).edit();
    SharedPreferences.Editor localEditor2 = localEditor1.putBoolean(paramString, paramBoolean);
    boolean bool = localEditor1.commit();
  }

  void setLauncher(Launcher paramLauncher)
  {
    this.mLauncher = paramLauncher;
  }

  public void setOnLongClickListener(View.OnLongClickListener paramOnLongClickListener)
  {
    this.mLongClickListener = paramOnLongClickListener;
    int i = getChildCount();
    int j = 0;
    while (j < i)
    {
      getChildAt(j).setOnLongClickListener(paramOnLongClickListener);
      j += 1;
    }
  }

  public void setPageIndicator(PageIndicatorView paramPageIndicatorView)
  {
    this.mPageIndicator = paramPageIndicatorView;
  }

  public void setTransitionType(int paramInt)
  {
    this.m_transitionType = paramInt;
    this.m_savedTransitionType = paramInt;
  }

  void snapToScreen(int paramInt)
  {
    snapToScreen(paramInt, 0, 0, 0);
  }

  void snapToScreen(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    snapToScreen(paramInt1, paramInt2, 0, paramBoolean);
  }

  void snapToScreen(int paramInt, boolean paramBoolean)
  {
    snapToScreen(paramInt, 0, 0, paramBoolean);
  }

  void startDrag(CellLayout.CellInfo paramCellInfo)
  {
    View localView1 = paramCellInfo.cell;
    if (!localView1.isInTouchMode());
    while (true)
    {
      return;
      this.mDragInfo = paramCellInfo;
      CellLayout.CellInfo localCellInfo = this.mDragInfo;
      int i = this.mCurrentScreen;
      localCellInfo.screen = i;
      int j = this.mCurrentScreen;
      ((CellLayout)getChildAt(j)).onDragChild(localView1);
      DragController localDragController = this.mDragController;
      Object localObject = localView1.getTag();
      int k = DragController.DRAG_ACTION_MOVE;
      View localView2 = localDragController.startDrag(localView1, this, localObject, k);
      invalidate();
    }
  }

  protected void transitionCube(Canvas paramCanvas, int paramInt1, int paramInt2, float paramFloat)
  {
    Workspace localWorkspace1 = this;
    int i = paramInt1;
    View localView1 = localWorkspace1.getChildAt(i);
    Workspace localWorkspace2 = this;
    int j = paramInt2;
    View localView2 = localWorkspace2.getChildAt(j);
    int k = getChildCount();
    int l = localView1.getLeft();
    int i1;
    if (localView2 != null)
    {
      i1 = localView2.getLeft();
      label55: if (!this.m_isCycle)
        break label342;
      int i2 = k - 1;
      int i3 = paramInt1;
      int i4 = i2;
      if ((i3 == i4) && (paramInt2 == 0))
      {
        if (paramFloat <= 0.0F)
          break label324;
        int i5 = getWidth();
        i1 = k * i5;
      }
      label107: float f1 = 0.0F;
      if (localView1 != null)
      {
        if (paramFloat < 0.0F)
          paramFloat += 1.0F;
        float f2 = localView1.getMeasuredWidth();
        float f3 = localView1.getMeasuredHeight() * 0.5F;
        f1 = -90.0F * paramFloat;
        float f4 = Math.abs(paramFloat);
        float f5 = (1.0F - f4) * 0.8F + 0.2F;
        int i6 = (int)(255.0F * f5);
        Workspace localWorkspace3 = this;
        Canvas localCanvas1 = paramCanvas;
        localWorkspace3.transitionDrawChild(localCanvas1, localView1, l, f1, 0.0F, 1.0F, 0.0F, f2, f3, i6);
      }
      if (localView2 != null)
      {
        float f6 = localView2.getMeasuredHeight() * 0.5F;
        float f7 = f1 + 90.0F;
        float f8 = Math.abs(paramFloat) * 0.8F + 0.2F;
        int i7 = (int)(255.0F * f8);
        Workspace localWorkspace4 = this;
        Canvas localCanvas2 = paramCanvas;
        float f9 = f7;
        float f10 = 0.0F;
        float f11 = f6;
        localWorkspace4.transitionDrawChild(localCanvas2, localView2, i1, f9, 0.0F, 1.0F, 0.0F, f10, f11, i7);
      }
    }
    while (true)
    {
      return;
      i1 = 0;
      break label55:
      label324: if (paramFloat < 0.0F);
      l = -getWidth();
      break label107:
      label342: if (((paramInt1 == 0) && (paramFloat < 0.0F)) || (localView2 == null));
      Workspace localWorkspace5 = this;
      Canvas localCanvas3 = paramCanvas;
      View localView3 = localView1;
      int i8 = l;
      localWorkspace5.transitionDrawChild(localCanvas3, localView3, i8);
    }
  }

  protected void transitionFade(Canvas paramCanvas, int paramInt1, int paramInt2, float paramFloat)
  {
    Matrix localMatrix = new Matrix();
    Workspace localWorkspace1 = this;
    int i = paramInt1;
    View localView1 = localWorkspace1.getChildAt(i);
    Workspace localWorkspace2 = this;
    int j = paramInt2;
    View localView2 = localWorkspace2.getChildAt(j);
    int k = getChildCount();
    int l = localView1.getLeft();
    int i1 = l;
    float f1 = Math.abs(paramFloat);
    float f2 = 1.0F - f1;
    float f3 = Math.abs(paramFloat);
    if (this.m_isCycle)
    {
      int i2 = k - 1;
      int i3 = paramInt1;
      int i4 = i2;
      if ((i3 == i4) && (paramInt2 == 0) && (paramFloat < 0.0F))
      {
        l = -getWidth();
        i1 = 0;
        f2 = Math.abs(paramFloat);
        float f4 = Math.abs(paramFloat);
        f3 = 1.0F - f4;
      }
    }
    while (true)
    {
      if ((localView2 != null) && (paramFloat != 0.0F))
      {
        float f5 = 0.5F + f2;
        float f6 = 0.5F / f5;
        float f7 = (1.0F - f6) / 0.6666666F;
        float f8 = 1.0F - f7;
        float f9 = 0.74F * f7;
        float f10 = f8 + f9;
        float f11 = getWidth();
        float f12 = paramFloat * f11;
        AccelerateInterpolator localAccelerateInterpolator1 = new android/view/animation/AccelerateInterpolator;
        AccelerateInterpolator localAccelerateInterpolator2 = localAccelerateInterpolator1;
        float f13 = 0.9F;
        localAccelerateInterpolator2.<init>(f13);
        AccelerateInterpolator localAccelerateInterpolator3 = localAccelerateInterpolator1;
        float f14 = f3;
        float f15 = localAccelerateInterpolator3.getInterpolation(f14);
        int i5 = localView2.getMeasuredWidth();
        int i6 = localView2.getMeasuredHeight();
        float f16 = i5 / 2.0F;
        float f17 = i6 / 2.0F;
        int i7 = (int)(255.0F * f15);
        Workspace localWorkspace3 = this;
        Canvas localCanvas1 = paramCanvas;
        localWorkspace3.transitionDrawChild(localCanvas1, localView2, i1, 0.0F, 0.0F, f10, f12, f16, f17, i7);
      }
      if (localView1 != null)
      {
        Workspace localWorkspace4 = this;
        Canvas localCanvas2 = paramCanvas;
        View localView3 = localView1;
        int i8 = l;
        localWorkspace4.transitionDrawChild(localCanvas2, localView3, i8);
      }
      return;
      if ((paramInt1 != 0) || (paramFloat >= 0.0F))
        continue;
      localView2 = null;
    }
  }

  protected void transitionFlip(Canvas paramCanvas, int paramInt1, int paramInt2, float paramFloat)
  {
    Workspace localWorkspace1 = this;
    int i = paramInt1;
    View localView1 = localWorkspace1.getChildAt(i);
    Workspace localWorkspace2 = this;
    int j = paramInt2;
    View localView2 = localWorkspace2.getChildAt(j);
    int k = getChildCount();
    int l = localView1.getLeft();
    int i1;
    label55: label107: View localView4;
    int i7;
    if (localView2 != null)
    {
      i1 = localView2.getLeft();
      if (!this.m_isCycle)
        break label303;
      int i2 = k - 1;
      int i3 = paramInt1;
      int i4 = i2;
      if ((i3 == i4) && (paramInt2 == 0))
      {
        if (paramFloat <= 0.0F)
          break label285;
        int i5 = getWidth();
        i1 = k * i5;
      }
      View localView3 = localView1;
      localView4 = localView2;
      int i6 = l;
      i7 = i1;
      if (paramFloat < 0.0F)
      {
        localView3 = localView2;
        localView4 = localView1;
        i6 = i1;
        i7 = l;
      }
      if (Math.abs(paramFloat) >= 0.5F)
        break label348;
      float f1 = 2.0F * paramFloat;
      float f2 = localView3.getMeasuredWidth() * 0.5F;
      float f3 = localView3.getMeasuredHeight() * 0.5F;
      float f4 = -90.0F * f1;
      float f5 = Math.abs(paramFloat);
      float f6 = (1.0F - f5) * 0.8F + 0.2F;
      float f7 = localView3.getMeasuredWidth();
      float f8 = paramFloat * f7;
      int i8 = (int)(255.0F * f6);
      Workspace localWorkspace3 = this;
      Canvas localCanvas1 = paramCanvas;
      localWorkspace3.transitionDrawChild(localCanvas1, localView3, i6, f4, 0.0F, 1.0F, f8, f2, f3, i8);
    }
    while (true)
    {
      label278: return;
      i1 = 0;
      break label55:
      label285: if (paramFloat < 0.0F);
      l = -getWidth();
      break label107:
      label303: if (((paramInt1 == 0) && (paramFloat < 0.0F)) || (localView2 == null));
      Workspace localWorkspace4 = this;
      Canvas localCanvas2 = paramCanvas;
      View localView5 = localView1;
      int i9 = l;
      localWorkspace4.transitionDrawChild(localCanvas2, localView5, i9);
    }
    label348: float f9;
    float f12;
    if (paramFloat > 0.0F)
    {
      f9 = (paramFloat - 0.5F) * 2.0F - 1.0F;
      float f10 = paramFloat - 1.0F;
      float f11 = localView4.getMeasuredWidth();
      f12 = f10 * f11;
    }
    while (true)
    {
      float f13 = localView4.getMeasuredWidth() * 0.5F;
      float f14 = localView4.getMeasuredHeight() * 0.5F;
      float f15 = -90.0F * f9;
      float f16 = Math.abs(f9);
      float f17 = (1.0F - f16) * 0.8F + 0.2F;
      int i10 = (int)(255.0F * f17);
      Workspace localWorkspace5 = this;
      Canvas localCanvas3 = paramCanvas;
      float f18 = f15;
      float f19 = f13;
      float f20 = f14;
      localWorkspace5.transitionDrawChild(localCanvas3, localView4, i7, f18, 0.0F, 1.0F, f12, f19, f20, i10);
      break label278:
      f9 = (0.5F + paramFloat) * 2.0F + 1.0F;
      float f21 = 1.0F + paramFloat;
      float f22 = localView4.getMeasuredWidth();
      f12 = f21 * f22;
    }
  }

  protected void transitionPhotowall(Canvas paramCanvas, int paramInt1, int paramInt2, float paramFloat)
  {
    Workspace localWorkspace1 = this;
    int i = paramInt1;
    View localView1 = localWorkspace1.getChildAt(i);
    Workspace localWorkspace2 = this;
    int j = paramInt2;
    View localView2 = localWorkspace2.getChildAt(j);
    int k = getChildCount();
    int l = localView1.getLeft();
    int i1;
    label55: label107: float f1;
    View localView3;
    View localView4;
    int i6;
    int i7;
    label194: float f6;
    float f8;
    if (localView2 != null)
    {
      i1 = localView2.getLeft();
      if (!this.m_isCycle)
        break label446;
      int i2 = k - 1;
      int i3 = paramInt1;
      int i4 = i2;
      if ((i3 == i4) && (paramInt2 == 0))
      {
        if (paramFloat <= 0.0F)
          break label428;
        int i5 = getWidth();
        i1 = k * i5;
      }
      f1 = 0.0F;
      localView3 = localView1;
      localView4 = localView2;
      i6 = l;
      i7 = i1;
      if (paramFloat < 0.0F)
      {
        localView3 = localView2;
        localView4 = localView1;
        i6 = i1;
        i7 = l;
      }
      if (Math.abs(paramFloat) <= 0.5F)
        break label499;
      float f2 = Math.abs(paramFloat) - 0.5F;
      float f3 = 60.0F * f2 * 2.0F;
      f1 = 60.0F - f3;
      float f4 = localView3.getMeasuredWidth() * 0.5F;
      float f5 = localView4.getMeasuredWidth() * paramFloat;
      f6 = f4 + f5;
      if (paramFloat <= 0.0F)
        break label519;
      float f7 = localView4.getMeasuredWidth();
      f8 = f6 - f7;
    }
    while (true)
    {
      if (localView3 != null)
      {
        float f9 = f6;
        float f10 = localView3.getMeasuredHeight() * 0.5F;
        float f11 = Math.abs(paramFloat);
        float f12 = (1.0F - f11) * 0.8F + 0.2F;
        int i8 = (int)(255.0F * f12);
        Workspace localWorkspace3 = this;
        Canvas localCanvas1 = paramCanvas;
        localWorkspace3.transitionDrawChild(localCanvas1, localView3, i6, f1, 0.0F, 1.0F, 0.0F, f9, f10, i8);
      }
      if (localView4 != null)
      {
        float f13 = f8;
        float f14 = localView4.getMeasuredHeight() * 0.5F;
        float f15 = Math.abs(paramFloat) * 0.8F + 0.2F;
        int i9 = (int)(255.0F * f15);
        Workspace localWorkspace4 = this;
        Canvas localCanvas2 = paramCanvas;
        float f16 = f1;
        float f17 = f13;
        float f18 = f14;
        localWorkspace4.transitionDrawChild(localCanvas2, localView4, i7, f16, 0.0F, 1.0F, 0.0F, f17, f18, i9);
      }
      while (true)
      {
        return;
        i1 = 0;
        break label55:
        label428: if (paramFloat < 0.0F);
        l = -getWidth();
        break label107:
        label446: if (((paramInt1 == 0) && (paramFloat < 0.0F)) || (localView2 == null) || (paramFloat == 0.0F));
        Workspace localWorkspace5 = this;
        Canvas localCanvas3 = paramCanvas;
        View localView5 = localView1;
        int i10 = l;
        localWorkspace5.transitionDrawChild(localCanvas3, localView5, i10);
      }
      label499: if (Math.abs(paramFloat) > 0.01F);
      f1 = 60.0F;
      break label194:
      label519: float f19 = localView4.getMeasuredWidth();
      f8 = f6 + f19;
    }
  }

  protected void transitionWave(Canvas paramCanvas, int paramInt1, int paramInt2, float paramFloat)
  {
  }

  protected void transitionWindmill(Canvas paramCanvas, int paramInt1, int paramInt2, float paramFloat)
  {
    Workspace localWorkspace1 = this;
    int i = paramInt1;
    View localView1 = localWorkspace1.getChildAt(i);
    Workspace localWorkspace2 = this;
    int j = paramInt2;
    View localView2 = localWorkspace2.getChildAt(j);
    int k = getChildCount();
    int l = localView1.getLeft();
    int i1;
    label55: label107: View localView4;
    int i7;
    if (localView2 != null)
    {
      i1 = localView2.getLeft();
      if (!this.m_isCycle)
        break label432;
      int i2 = k - 1;
      int i3 = paramInt1;
      int i4 = i2;
      if ((i3 == i4) && (paramInt2 == 0))
      {
        if (paramFloat <= 0.0F)
          break label414;
        int i5 = getWidth();
        i1 = k * i5;
      }
      View localView3 = localView1;
      localView4 = localView2;
      int i6 = l;
      i7 = i1;
      if (paramFloat < 0.0F)
      {
        localView3 = localView2;
        localView4 = localView1;
        i6 = i1;
        i7 = l;
      }
      if (localView3 != null)
      {
        float f1 = 45.0F * paramFloat;
        float f2 = localView3.getMeasuredWidth() * 0.5F;
        float f3 = localView3.getMeasuredHeight() * 2.0F;
        float f4 = Math.abs(paramFloat);
        float f5 = (1.0F - f4) * 0.8F + 0.2F;
        float f6 = localView3.getMeasuredWidth();
        float f7 = paramFloat * f6;
        int i8 = (int)(255.0F * f5);
        Workspace localWorkspace3 = this;
        Canvas localCanvas1 = paramCanvas;
        localWorkspace3.transitionDrawChild(localCanvas1, localView3, i6, 0.0F, f1, 1.0F, f7, f2, f3, i8);
      }
      if (paramFloat < 0.0F)
        break label477;
      paramFloat -= 1.0F;
    }
    while (true)
    {
      if (localView4 != null)
      {
        float f8 = 45.0F * paramFloat;
        float f9 = localView4.getMeasuredWidth() * 0.5F;
        float f10 = localView4.getMeasuredHeight() * 2.0F;
        float f11 = Math.abs(paramFloat);
        float f12 = (1.0F - f11) * 0.8F + 0.2F;
        float f13 = localView4.getMeasuredWidth();
        float f14 = paramFloat * f13;
        int i9 = (int)(255.0F * f12);
        Workspace localWorkspace4 = this;
        Canvas localCanvas2 = paramCanvas;
        float f15 = f8;
        float f16 = f9;
        float f17 = f10;
        localWorkspace4.transitionDrawChild(localCanvas2, localView4, i7, 0.0F, f15, 1.0F, f14, f16, f17, i9);
      }
      while (true)
      {
        return;
        i1 = 0;
        break label55:
        label414: if (paramFloat < 0.0F);
        l = -getWidth();
        break label107:
        label432: if (((paramInt1 == 0) && (paramFloat < 0.0F)) || (localView2 == null));
        Workspace localWorkspace5 = this;
        Canvas localCanvas3 = paramCanvas;
        View localView5 = localView1;
        int i10 = l;
        localWorkspace5.transitionDrawChild(localCanvas3, localView5, i10);
      }
      label477: paramFloat += 1.0F;
    }
  }

  void updateShortcuts(ArrayList<ApplicationInfo> paramArrayList)
  {
    if (paramArrayList == null)
      return;
    PackageManager localPackageManager = this.mLauncher.getPackageManager();
    int i = getChildCount();
    int j = 0;
    while (true)
    {
      if (j < i);
      Workspace localWorkspace = this;
      int k = j;
      CellLayout localCellLayout = (CellLayout)localWorkspace.getChildAt(k);
      int l = localCellLayout.getChildCount();
      int i1 = 0;
      while (i1 < l)
      {
        View localView = localCellLayout.getChildAt(i1);
        Object localObject = localView.getTag();
        ShortcutInfo localShortcutInfo1;
        Intent localIntent1;
        ComponentName localComponentName1;
        if (localObject instanceof ShortcutInfo)
        {
          localShortcutInfo1 = (ShortcutInfo)localObject;
          localIntent1 = localShortcutInfo1.intent;
          localComponentName1 = localIntent1.getComponent();
          if (localShortcutInfo1.itemType != 0)
            break label540;
          String str1 = localIntent1.getAction();
          if ((!"android.intent.action.MAIN".equals(str1)) || (localComponentName1 == null))
            break label540;
          int i2 = paramArrayList.size();
          int i3 = 0;
          while (true)
          {
            if (i3 >= i2)
              break label540;
            ArrayList<ApplicationInfo> localArrayList = paramArrayList;
            int i4 = i3;
            ComponentName localComponentName2 = ((ApplicationInfo)localArrayList.get(i4)).componentName;
            ComponentName localComponentName3 = localComponentName1;
            if (localComponentName2.equals(localComponentName3))
            {
              IconCache localIconCache1 = this.mIconCache;
              Intent localIntent2 = localShortcutInfo1.intent;
              Bitmap localBitmap1 = localIconCache1.getIcon(localIntent2);
              ShortcutInfo localShortcutInfo2 = localShortcutInfo1;
              Bitmap localBitmap2 = localBitmap1;
              localShortcutInfo2.setIcon(localBitmap2);
              TextView localTextView1 = (TextView)localView;
              Resources localResources1 = getResources();
              IconCache localIconCache2 = this.mIconCache;
              ShortcutInfo localShortcutInfo3 = localShortcutInfo1;
              IconCache localIconCache3 = localIconCache2;
              Bitmap localBitmap3 = localShortcutInfo3.getIcon(localIconCache3);
              BitmapDrawable localBitmapDrawable1 = new BitmapDrawable(localResources1, localBitmap3);
              localTextView1.setCompoundDrawablesWithIntrinsicBounds(null, localBitmapDrawable1, null, null);
            }
            i3 += 1;
          }
        }
        if (localObject instanceof UserFolderInfo)
        {
          UserFolderInfo localUserFolderInfo = (UserFolderInfo)localObject;
          Iterator localIterator1 = localUserFolderInfo.contents.iterator();
          String str2;
          do
          {
            do
            {
              if (!localIterator1.hasNext())
                break label532;
              localShortcutInfo1 = (ShortcutInfo)localIterator1.next();
              localIntent1 = localShortcutInfo1.intent;
              localComponentName1 = localIntent1.getComponent();
            }
            while (localShortcutInfo1.itemType != 0);
            str2 = localIntent1.getAction();
          }
          while ((!"android.intent.action.MAIN".equals(str2)) || (localComponentName1 == null));
          Iterator localIterator2 = paramArrayList.iterator();
          while (true)
          {
            if (localIterator2.hasNext());
            ComponentName localComponentName4 = ((ApplicationInfo)localIterator2.next()).componentName;
            ComponentName localComponentName5 = localComponentName1;
            if (!localComponentName4.equals(localComponentName5))
              continue;
            IconCache localIconCache4 = this.mIconCache;
            Intent localIntent3 = localShortcutInfo1.intent;
            Bitmap localBitmap4 = localIconCache4.getIcon(localIntent3);
            ShortcutInfo localShortcutInfo4 = localShortcutInfo1;
            Bitmap localBitmap5 = localBitmap4;
            localShortcutInfo4.setIcon(localBitmap5);
            TextView localTextView2 = (TextView)localView;
            Resources localResources2 = getResources();
            IconCache localIconCache5 = this.mIconCache;
            ShortcutInfo localShortcutInfo5 = localShortcutInfo1;
            IconCache localIconCache6 = localIconCache5;
            Bitmap localBitmap6 = localShortcutInfo5.getIcon(localIconCache6);
            BitmapDrawable localBitmapDrawable2 = new BitmapDrawable(localResources2, localBitmap6);
            localTextView2.setCompoundDrawablesWithIntrinsicBounds(null, localBitmapDrawable2, null, null);
          }
          label532: localUserFolderInfo.mFolderIcon.updateFolderIcon();
        }
        label540: i1 += 1;
      }
      j += 1;
    }
  }


  class WorkspaceOvershootInterpolator
    implements Interpolator
  {
    private float mTension = 1.2F;

    public float getInterpolation(float paramFloat)
    {
      float f1 = paramFloat - 1.0F;
      float f2 = f1 * f1 * f1 * f1;
      float f3 = (this.mTension + 1.0F) * f1;
      float f4 = this.mTension;
      float f5 = f3 + f4;
      return f2 * f5 + 1.0F;
    }
  }
}*/