package com.tagsoft.mazegame;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class JoystickView extends View implements Runnable {

    //INTERFACES

    //조이스틱 버튼이 움직였을 때 호출되는 콜백에 대한 인터페이스 정의
    public interface OnMoveListener {
        //조이스틱의 버튼이 움직였을 때 호출
        //파라미터 angle: 현재 각도
        //파라미터 strength: 현재 강도
        void onMove(int angle, int strength);
    }

    //조이스틱뷰가 터치되고 multiple pointers가 유지될 때 호출되는 콜백에 대한 인터페이스 정의
    public interface OnMultipleLongPressListener {
        //조이스틱뷰가 눌리고 multiple pointers로 충분한 시간동안 유지되면 호출
        void onMultipleLongPress();
    }

    //CONSTANTS

    //기본 새로 고침 속도 - 콜백을 통해 이동 값을 전송하는 시간(밀리초)
    private static final int DEFAULT_LOOP_INTERVAL = 50; // in milliseconds

    //MultipleLongPress를 취소하지 않고 약간의 이동을 허용하는 데 사용
    private static final int MOVE_TOLERANCE = 10;

    //버튼의 기본 색
    private static final int DEFAULT_COLOR_BUTTON = Color.BLACK;

    //버튼 바깥쪽부분의 기본 색깔
    private static final int DEFAULT_COLOR_BORDER = Color.TRANSPARENT;  //투명

    //바깥쪽 기본 알파값
    private static final int DEFAULT_ALPHA_BORDER = 255;

    //기본 배경 색깔
    private static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;

    //기본 뷰 크기
    private static final int DEFAULT_SIZE = 200;

    //바깥쪽 기본 크기
    private static final int DEFAULT_WIDTH_BORDER = 3;

    //중심 고정에 대한 기본 동작(자동 정의 아님)
    private static final boolean DEFAULT_FIXED_CENTER = true;

    //자동으로 버튼이 다시 중앙으로 이동하는 기본 동작(자동으로 버튼이 이동)
    private static final boolean DEFAULT_AUTO_RECENTER_BUTTON = true;

    //Default behavior to button stickToBorder (button stay on the border)
    private static final boolean DEFAULT_BUTTON_STICK_TO_BORDER = false;

    // DRAWING
    private Paint mPaintCircleButton;
    private Paint mPaintCircleBorder;
    private Paint mPaintBackground;

    private Paint mPaintBitmapButton;
    private Bitmap mButtonBitmap;

    //버튼 크기 정의에 사용하는 비율
    private float mButtonSizeRatio;

    //배경 크기 정의에 사용하는 비율
    private float mBackgroundSizeRatio;


    //좌표
    private int mPosX = 0;
    private int mPosY = 0;
    private int mCenterX = 0;
    private int mCenterY = 0;

    private int mFixedCenterX = 0;
    private int mFixedCenterY = 0;

    //자동 중심(거짓)이든 고정 중심(참)이든 행동 받아들이는데 사용
    private boolean mFixedCenter;

    //버튼이 자동으로 중심으로 돌아가는 행동을 받아들이는데 사용 (DEFAULT_AUTO_RECENTER_BUTTON = true;)
    //버튼을 놓거나 false일 때
    private boolean mAutoReCenterButton;

    //버튼이 테두리(참)에 붙어 있거나 어디에나 있을 수 있는 동작(false - 일반 동작과 유사한 경우)을 조정하기 위해 사용됨
    private boolean mButtonStickToBorder;

    //조이스틱을 활성화/비활성화하는 데 사용. 비활성화(enabled = false) 시 조이스틱 버튼이 이동할 수 없으며 onMove()를 호출하지 않는다.
    private boolean mEnabled;

    // 크기
    private int mButtonRadius;
    private int mBorderRadius;

    //경계의 알파(동적으로 색을 변경할 때 사용)
    private int mBorderAlpha;

    //mBorderRadius를 기반으로 하지만 약간 더 작게(경계 획 크기의 절반 이하)
    private float mBackgroundRadius;

    //OnMove 이벤트를 발송하는 데 사용되는 리스너
    private OnMoveListener mCallback;

    private long mLoopInterval = DEFAULT_LOOP_INTERVAL;
    private Thread mThread = new Thread(this);

    //MultipleLongPress event 발송하는 데 사용되는 리스너
    private OnMultipleLongPressListener mOnMultipleLongPressListener;

    private final Handler mHandlerMultipleLongPress = new Handler();
    private Runnable mRunnableMultipleLongPress;
    private int mMoveTolerance;


    //기본 값
    //수직, 수평 이동 모두 해당
    public static int BUTTON_DIRECTION_BOTH = 0;

    //이 파라미터 값에 의해서 버튼의 방향이 허용된다(마이너스 값: 수평 축, 플러스 값: 수직 축, 0: 양 축 모두)
    private int mButtonDirection = 0;

    //생성자

    //코드로 JoystickView를 만들어 사용할기 편한 생성자
    //null을 Attribute로 전달하는 다른 생성자 호출
    //파라미터의 context: JoystickView가 실행 중인 컨텍스트에서 현재 테마, 리소스 등에 액세스할 수 있다.
    public JoystickView(Context context) {
        this(context, null);
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    //XML에서 JoystickView가 만들어질때 호출되는 생성자
    //XML파일에서 JoystickView가 생성될 때 불려진다. 제공된 attributes는 XML파일에 지정된 속성 제공
    //파라미터의 context: JoystickView가 실행 중인 컨텍스트에서 현재 테마, 리소스 등에 액세스할 수 있다.
    //파라미터의 attrs: JoystickView를 만드는 XML 태그 속성
    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.JoystickView,
                0, 0
        );

        int buttonColor;
        int borderColor;
        int backgroundColor;
        int borderWidth;
        Drawable buttonDrawable;
        try {
            buttonColor = styledAttributes.getColor(R.styleable.JoystickView_JV_buttonColor, DEFAULT_COLOR_BUTTON);
            borderColor = styledAttributes.getColor(R.styleable.JoystickView_JV_borderColor, DEFAULT_COLOR_BORDER);
            mBorderAlpha = styledAttributes.getInt(R.styleable.JoystickView_JV_borderAlpha, DEFAULT_ALPHA_BORDER);
            backgroundColor = styledAttributes.getColor(R.styleable.JoystickView_JV_backgroundColor, DEFAULT_BACKGROUND_COLOR);
            borderWidth = styledAttributes.getDimensionPixelSize(R.styleable.JoystickView_JV_borderWidth, DEFAULT_WIDTH_BORDER);
            mFixedCenter = styledAttributes.getBoolean(R.styleable.JoystickView_JV_fixedCenter, DEFAULT_FIXED_CENTER);
            mAutoReCenterButton = styledAttributes.getBoolean(R.styleable.JoystickView_JV_autoReCenterButton, DEFAULT_AUTO_RECENTER_BUTTON);
            mButtonStickToBorder = styledAttributes.getBoolean(R.styleable.JoystickView_JV_buttonStickToBorder, DEFAULT_BUTTON_STICK_TO_BORDER);
            buttonDrawable = styledAttributes.getDrawable(R.styleable.JoystickView_JV_buttonImage);
            mEnabled = styledAttributes.getBoolean(R.styleable.JoystickView_JV_enabled, true);
            mButtonSizeRatio = styledAttributes.getFraction(R.styleable.JoystickView_JV_buttonSizeRatio, 1, 1, 0.25f);
            mBackgroundSizeRatio = styledAttributes.getFraction(R.styleable.JoystickView_JV_backgroundSizeRatio, 1, 1, 0.75f);
            mButtonDirection = styledAttributes.getInteger(R.styleable.JoystickView_JV_buttonDirection, BUTTON_DIRECTION_BOTH);
        } finally {
            styledAttributes.recycle();
        }

        // 속성에 따라 그리기 초기화
        mPaintCircleButton = new Paint();
        mPaintCircleButton.setAntiAlias(true);
        mPaintCircleButton.setColor(buttonColor);
        mPaintCircleButton.setStyle(Paint.Style.FILL);

        if (buttonDrawable != null) {
            if (buttonDrawable instanceof BitmapDrawable) {
                mButtonBitmap = ((BitmapDrawable) buttonDrawable).getBitmap();
                mPaintBitmapButton = new Paint();
            }
        }

        mPaintCircleBorder = new Paint();
        mPaintCircleBorder.setAntiAlias(true);
        mPaintCircleBorder.setColor(borderColor);
        mPaintCircleBorder.setStyle(Paint.Style.STROKE);
        mPaintCircleBorder.setStrokeWidth(borderWidth);

        if (borderColor != Color.TRANSPARENT) {
            mPaintCircleBorder.setAlpha(mBorderAlpha);
        }

        mPaintBackground = new Paint();
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setColor(backgroundColor);
        mPaintBackground.setStyle(Paint.Style.FILL);


        //MultiLongPress 동작 초기화
        mRunnableMultipleLongPress = new Runnable() {
            @Override
            public void run() {
                if (mOnMultipleLongPressListener != null)
                    mOnMultipleLongPressListener.onMultipleLongPress();
            }
        };
    }


    private void initPosition() {
        //중심 얻어오기
        mFixedCenterX = mCenterX = mPosX = getWidth() / 2;
        mFixedCenterY = mCenterY = mPosY = getWidth() / 2;
    }


    //배경 그리기, 버튼과 경계
    //파라미터의 canvas: 도형이 그려질 캔버스
    @Override
    protected void onDraw(Canvas canvas) {
        //배경 그리기
        canvas.drawCircle(mFixedCenterX, mFixedCenterY, mBackgroundRadius, mPaintBackground);

        //둥근 경계 그리기
        canvas.drawCircle(mFixedCenterX, mFixedCenterY, mBorderRadius, mPaintCircleBorder);

        //이미지로 버튼 그리기
        if (mButtonBitmap != null) {
            canvas.drawBitmap(
                    mButtonBitmap,
                    mPosX + mFixedCenterX - mCenterX - mButtonRadius,
                    mPosY + mFixedCenterY - mCenterY - mButtonRadius,
                    mPaintBitmapButton
            );
        }
        //기본 원으로 버튼 그리기
        else {
            canvas.drawCircle(
                    mPosX + mFixedCenterX - mCenterX,
                    mPosY + mFixedCenterY - mCenterY,
                    mButtonRadius,
                    mPaintCircleButton
            );
        }
    }

    //뷰에 크기가 변하면 호출
    //여기서 모든 도형을 그릴 뷰의 중심과 반경을 얻을 수 있다
    //파라미터의 w: 현재 이 뷰의 너비
    //파라미터의 h: 현재 이 뷰의 높이
    //파라미터의 oldW: 이전 뷰의 너비
    //파라미터의 oldH: 이전 뷰의 높이
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        initPosition();

        //최소 크기에 따른 반지름: 높이 또는 너비
        int d = Math.min(w, h);
        mButtonRadius = (int) (d / 2 * mButtonSizeRatio);
        mBorderRadius = (int) (d / 2 * mBackgroundSizeRatio);
        mBackgroundRadius = mBorderRadius - (mPaintCircleBorder.getStrokeWidth() / 2);

        if (mButtonBitmap != null)
            mButtonBitmap = Bitmap.createScaledBitmap(mButtonBitmap, mButtonRadius * 2, mButtonRadius * 2, true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //뷰의 크기를 특정 폭 및 높이로 조정하기 위해 측정된 값 설정
        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));
        setMeasuredDimension(d, d);
    }


    private int measure(int measureSpec) {
        if (MeasureSpec.getMode(measureSpec) == MeasureSpec.UNSPECIFIED) {
            //한도가 지정되지 않은 경우 기본 크기 반환(200)
            return DEFAULT_SIZE;
        } else {
            //사용 가능한 공간을 채우려면 항상 사용 가능한 전체 범위를 반환하십시오.
            return MeasureSpec.getSize(measureSpec);
        }
    }

    //USER EVENT

    //터치 스크린 모션 이벤트
    //손가락 좌표에 따라 버튼을 이동하고 다중포인터로만 longPress를 감지
    //파라미터의 event: 모션 이벤트
    //리턴: 만약 이벤트가 처리되면 참, 아니면 거짓
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //만약 disabled이면 움직이지 않음
        if (!mEnabled) {
            return true;
        }

        //손가락 좌표에 따라 버튼을 이동 및 방향 옵션에 따라 한 축을 제한 시킨다
        mPosY = mButtonDirection < 0 ? mCenterY : (int) event.getY(); // 음의 방향은 수평 축
        mPosX = mButtonDirection > 0 ? mCenterX : (int) event.getX(); // 양의 방향은 수직 축

        if (event.getAction() == MotionEvent.ACTION_UP) {

            //터치 스크린에서 손가락을 땠을 때 listener를 멈춤
            mThread.interrupt();

            //버튼을 중앙에 다시 맞추거나 설정에 따른다
            if (mAutoReCenterButton) {
                resetButtonPosition();

                // 리셋버튼 후 0이어야 해서 최종 강도 및 각도를 지금 반영
                if (mCallback != null)
                    mCallback.onMove(getAngle(), getStrength());
            }

            //mAutoReCenterButton이 거짓이면 새로운 위치 X와 Y를 처리한 후에만 마지막 힘과 각도를 보낼 것이다. 그렇지 않으면 경계 한계보다 높을 수 있다.
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mThread != null && mThread.isAlive()) {
                mThread.interrupt();
            }

            mThread = new Thread(this);
            mThread.start();

            if (mCallback != null)
                mCallback.onMove(getAngle(), getStrength());
        }

        //멀티 터치로만 첫 터치와 길게 누름을 handle
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 첫 터치가 발생했을 때 중심 반영 (만약 auto-defined center이 세팅되어 있다면)
                if (!mFixedCenter) {
                    mCenterX = mPosX;
                    mCenterY = mPosY;
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN: {
                //두번째 손가락 터치가 발생했을 때
                if (event.getPointerCount() == 2) {
                    mHandlerMultipleLongPress.postDelayed(mRunnableMultipleLongPress, ViewConfiguration.getLongPressTimeout()*2);
                    mMoveTolerance = MOVE_TOLERANCE;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE:
                mMoveTolerance--;
                if (mMoveTolerance == 0) {
                    mHandlerMultipleLongPress.removeCallbacks(mRunnableMultipleLongPress);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP: {
                // 마지막 다중 터치가 헤제될 때
                if (event.getPointerCount() == 2) {
                    mHandlerMultipleLongPress.removeCallbacks(mRunnableMultipleLongPress);
                }
                break;
            }
        }

        double abs = Math.sqrt((mPosX - mCenterX) * (mPosX - mCenterX)
                + (mPosY - mCenterY) * (mPosY - mCenterY));

        // (abs > mBorderRadius) 버튼이 너무 멀어서 border로 제한한다는 의미.
        // (buttonStickBorder && abs != 0) means wherever is the button we stick it to the border except when abs == 0
        if (abs > mBorderRadius || (mButtonStickToBorder && abs != 0)) {
            mPosX = (int) ((mPosX - mCenterX) * mBorderRadius / abs + mCenterX);
            mPosY = (int) ((mPosY - mCenterY) * mBorderRadius / abs + mCenterY);
        }

        if (!mAutoReCenterButton) {
            //중앙으로 재설정되지 않은 경우 마지막 강도 및 각도를 지금 업데이트
            if (mCallback != null)
                mCallback.onMove(getAngle(), getStrength());
        }

        //새로 그리기 강요
        invalidate();

        return true;
    }


    //GETTERS

    //360° 반시계형 프로토타입 규칙에 따라 각도를 처리한다.
    //리턴: 버튼의 각도
    private int getAngle() {
        int angle = (int) Math.toDegrees(Math.atan2(mCenterY - mPosY, mPosX - mCenterX));
        return angle < 0 ? angle + 360 : angle; // make it as a regular counter-clock protractor
    }

    //중심과 경계 사이의 거리의 백분율로 강도를 처리한다.
    //리턴: 버튼의 강도
    private int getStrength() {
        return (int) (100 * Math.sqrt((mPosX - mCenterX)
                * (mPosX - mCenterX) + (mPosY - mCenterY)
                * (mPosY - mCenterY)) / mBorderRadius);
    }

    //중앙으로 버튼위치 리셋
    public void resetButtonPosition() {
        mPosX = mCenterX;
        mPosY = mCenterY;
    }

    //버튼이 이동한 현재 방향 리턴
    //리턴: 실제로 방향에 해당하는 정수 반환(음의 값은 수평축, 양의 값은 수직 축, 0는 양 축)
    public int getButtonDirection() {
        return mButtonDirection;
    }


    //조이스틱의 상태 반환. 버튼이 움직이지 않으면 false
    public boolean isEnabled() {
        return mEnabled;
    }

    //버튼의 크기 반환(총 너비/높이의 비율로)
    //기본값: 0.25 (25%)
    //버튼 크기: 0.0 ~ 1.0의 값
    public float getButtonSizeRatio() {
        return mButtonSizeRatio;
    }

    //배경의 크기 반환(총 너비/높이의 비율로)
    //기본값: 0.75 (75%)
    //배경 크기: 0.0 ~ 1.0의 값
    public float getmBackgroundSizeRatio() {
        return mBackgroundSizeRatio;
    }

    //버튼의 자동 중앙 복귀하는 현재 행동 반환(자동으로 중앙 복귀하면 true, 아니면 false)
    public boolean isAutoReCenterButton() {
        return mAutoReCenterButton;
    }

    //버튼이 테두리에 붙어 있는 현재의 동작 반환(버튼이 경계에 있으면 true, 아니면 false)
    public boolean isButtonStickToBorder() {
        return mButtonStickToBorder;
    }

    //border 좌상단과 관련된 버튼 중심의 상대적 X 좌표 반환
    //리턴: 0 ~ 100으로 정규화된 X좌표
    public int getNormalizedX() {
        if (getWidth() == 0) {
            return 50;
        }
        return Math.round((mPosX-mButtonRadius)*100.0f/(getWidth()-mButtonRadius*2));
    }

    //border 좌상단과 관련된 버튼 중심의 상대적 Y 좌표 반환
    //리턴: 0 ~ 100으로 정규화된 Y좌표
    public int getNormalizedY() {
        if (getHeight() == 0) {
            return 50;
        }
        return Math.round((mPosY-mButtonRadius)*100.0f/(getHeight()-mButtonRadius*2));
    }

    //border의 알파 반환
    //리턴: 이전에 설정된 0과 255 사이의 정수여야 한다.
    public int getBorderAlpha() {
        return mBorderAlpha;
    }

    //SETTERS

    //drawable로 버튼에 이미지 세팅
    //파라미터 d: 골라온 이미지
    public void setButtonDrawable(Drawable d) {
        if (d != null) {
            if (d instanceof BitmapDrawable) {
                mButtonBitmap = ((BitmapDrawable) d).getBitmap();

                if (mButtonRadius != 0) {
                    mButtonBitmap = Bitmap.createScaledBitmap(
                            mButtonBitmap,
                            mButtonRadius * 2,
                            mButtonRadius * 2,
                            true);
                }

                if (mPaintBitmapButton != null)
                    mPaintBitmapButton = new Paint();
            }
        }
    }

    //이 JoystickView의 버튼 색 설정
    //파라미터 color: 버튼의 색
    public void setButtonColor(int color) {
        mPaintCircleButton.setColor(color);
        invalidate();
    }

    //이 JoystickView의 border 색 설정
    //파라미터 color: border의 색
    public void setBorderColor(int color) {
        mPaintCircleBorder.setColor(color);
        if (color != Color.TRANSPARENT) {
            mPaintCircleBorder.setAlpha(mBorderAlpha);
        }
        invalidate();
    }

    //이 JoystickView의 border 알파 설정
    //파라미터 alpha: 0과 255 사이의 border의 투명성
    public void setBorderAlpha(int alpha) {
        mBorderAlpha = alpha;
        mPaintCircleBorder.setAlpha(alpha);
        invalidate();
    }

    //이 JoystickView의 배경색 설정
    //파라미터 color: 배경색
    @Override
    public void setBackgroundColor(int color) {
        mPaintBackground.setColor(color);
        invalidate();
    }

    //이 JoystickView의 border 너비 설정
    //파라미터 width: border의 너비
    public void setBorderWidth(int width) {
        mPaintCircleBorder.setStrokeWidth(width);
        mBackgroundRadius = mBorderRadius - (width / 2.0f);
        invalidate();
    }

    //이 JoystickView의 버튼을 이동할 때 호출할 콜백을 등록
    //파리미터 l: 동작시킬 콜백
    public void setOnMoveListener(OnMoveListener l) {
        setOnMoveListener(l, DEFAULT_LOOP_INTERVAL);
    }

    //이 JoystickView의 버튼을 이동할 때 호출할 콜백을 등록
    //파리미터 l: 동작시킬 콜백
    //파라미터 loopInterval: 호출할 새로 고침 속도(밀리초)
    public void setOnMoveListener(OnMoveListener l, int loopInterval) {
        mCallback = l;
        mLoopInterval = loopInterval;
    }

    //이 JoystickView가 터치되고 multiple pointers가 유지될 때 호출할 콜백을 등록
    //파리미터 l: 동작시킬 콜백
    public void setOnMultiLongPressListener(OnMultipleLongPressListener l) {
        mOnMultipleLongPressListener = l;
    }

    //조이스틱 중심에 대한 행동 설정 (fixed 또는 auto-defined)
    //파리미터 fixedCenter: 참 - fixed center, 거짓 - 터치 다운 기반의 auto-defined center
    public void setFixedCenter(boolean fixedCenter) {
        // if we set to "fixed" we make sure to re-init position related to the width of the joystick
        if (fixedCenter) {
            initPosition();
        }
        mFixedCenter = fixedCenter;
        invalidate();
    }

    //조이스틱 사용여부
    //파라미터 enabled: false - 움직이지 않길 원함, true - 움직이길 원함
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    //조이스틱 버튼 크기 설정(실제 너비/높이 분수로)
    //기본값: 25%(0.25)
    //파라미터 newRatio: 0.0 ~ 1.0
    public void setButtonSizeRatio(float newRatio) {
        if (newRatio > 0.0f & newRatio <= 1.0f) {
            mButtonSizeRatio = newRatio;
        }
    }

    //조이스틱 배경 크기 설정(실제 너비/높이 분수로)
    //기본값: 75%(0.75)
    //배경이 이미지면 동작 안함
    //파라미터 newRatio: 0.0 ~ 1.0
    public void setBackgroundSizeRatio(float newRatio) {
        if (newRatio > 0.0f & newRatio <= 1.0f) {
            mBackgroundSizeRatio = newRatio;
        }
    }

    //버튼의 자동 중앙 복귀 행동 설정
    //파라미터 b: 자종으로 복귀하면 true, 아니면 false
    public void setAutoReCenterButton(boolean b) {
        mAutoReCenterButton = b;
    }

    //버튼이 테두리에 붙어 있는 현재동작 설정
    // 파라미터 b: 버튼이 경계에 있으면 true, 아니면 false
    public void setButtonStickToBorder(boolean b) {
        mButtonStickToBorder = b;
    }

    //버튼이 이동할 수 있도록 현재 승인된 방향 설정
    //파라미터 direction: 이 값이 승인된 방향을 정한다(음수 값 - 수평 축, 양수 값 - 수직 축, 0 - 양 축)
    public void setButtonDirection(int direction) {
        mButtonDirection = direction;
    }


    /*
    IMPLEMENTS
     */

    @Override // Runnable
    public void run() {
        while (!Thread.interrupted()) {
            post(new Runnable() {
                public void run() {
                    if (mCallback != null)
                        mCallback.onMove(getAngle(), getStrength());
                }
            });

            try {
                Thread.sleep(mLoopInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}