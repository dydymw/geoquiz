package com.geoquiz.tdme.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class CheatActivity extends AppCompatActivity {

    public static boolean[]count = new boolean[1000];

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.geoquiz.tdme.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOW =
            "com.geoquiz.tdme.android.geoquiz.answer_shown";
    private static final String KEY_MYANSWERIDTRUE = "myansweridtrue";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;

    private TextView mVersionTextView;
    private Button mShowAnswer;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOW, false);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mVersionTextView = (TextView) findViewById(R.id.version);
        mVersionTextView.setText(String.valueOf(Build.VERSION.SDK_INT));
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        if (savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_MYANSWERIDTRUE, false);
            if (mAnswerIsTrue) {
                mAnswerTextView.setText(R.string.true_button);
            } else {
                mAnswerTextView.setText(R.string.false_button);
            }
            setAnswerShownResult(true);
        }
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                count[QuizActivity.mCurrentIndex]=true;
                setAnswerShownResult(true);

                int cx =mShowAnswer.getWidth()/2;
                int cy = mShowAnswer.getHeight()/2;
                float radius = mShowAnswer.getWidth();
                Animator animator= ViewAnimationUtils
                        .createCircularReveal(mShowAnswer,cx,cy,radius,0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mShowAnswer.setVisibility(View.INVISIBLE);
                    }
                });
                animator.start();
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOW, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_MYANSWERIDTRUE, mAnswerIsTrue);
    }
}
