package aleksey.vasilev.apptest.views;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aleksey.vasilev.apptest.AppTestApplication;
import aleksey.vasilev.apptest.R;
import aleksey.vasilev.apptest.model.Balance;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setBoostButtonOnClick();
        setGetBTCOnClick();
        setBalance();
        setBeginOnClick();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setBeginOnClick() {
        final ImageButton beginButton = getView().findViewById(R.id.balance_button);
        final AppTestApplication appTestApplication = (AppTestApplication) getActivity().getApplication();
        beginButton.setOnClickListener(
                it -> {
                    appTestApplication.getExecutorService().execute(() -> {
                        final Balance balance = appTestApplication.getBalanceDao().getBalance();
                        final Balance incrementedBalance = getIncrementedBalance(balance);
                        appTestApplication.getBalanceDao().insertBalance(incrementedBalance);
                        getActivity().runOnUiThread(() -> setBalanceUI(incrementedBalance));
                    });
                }
        );
    }

    private Balance getIncrementedBalance(Balance balance) {
        final long satoshiInc = 15;
        final String btcInc = "0.00000015";
        final long percentInc = 5;
        BigDecimal satoshi = balance.valueSatoshi;
        BigDecimal btc = balance.valueBTC;
        long percent = balance.percent;
        satoshi = satoshi.add(new BigDecimal(satoshiInc));
        btc = btc.add(new BigDecimal(btcInc));
        percent = percent + percentInc;
        return new Balance(
                satoshi,
                btc,
                percent,
                System.currentTimeMillis()
        );
    }

    private void setGetBTCOnClick() {
        final AppTestApplication appTestApplication = (AppTestApplication) getActivity().getApplication();
        final ScheduledExecutorService executorService = appTestApplication.getExecutorService();
        final ImageButton getBTCButton = getView().findViewById(R.id.get_btc_button);
        getBTCButton.setOnClickListener((View) -> executorService.submit(
                () -> {
                    final Balance balance = appTestApplication.getBalanceDao().getBalance();
                    final String dataToSend = "https://play.google.com/store/apps/details?id=" + getContext().getPackageName() + "\n" +
                            getContext().getString(R.string.balance) + balance.valueSatoshi + " " + getContext().getString(R.string.satoshi) +
                            " OR " + balance.valueBTC + " " + getContext().getString(R.string.btc);
                    final Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, dataToSend);
                    sendIntent.setType("text/plain");
                    final Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
        )
        );
    }

    private void setBalance() {
        final AppTestApplication appTestApplication = (AppTestApplication) getActivity().getApplication();
        appTestApplication.getExecutorService().execute(() -> {
            Balance balance = appTestApplication.getBalanceDao().getBalance();
            getActivity().runOnUiThread(() -> setBalanceUI(balance));
        });
    }

    private void setBalanceUI(Balance balance) {
        final TextView satoshiBalance = getView().findViewById(R.id.balance_value_satoshi);
        final TextView btcBalance = getView().findViewById(R.id.balance_value_btc);
        final TextView percentageBalance = getView().findViewById(R.id.progress_percent);
        final ImageView progress_front = getView().findViewById(R.id.progress_front);
        final float density = getResources().getDisplayMetrics().density;
        final int progressBackWidthDPI = 39;
        final int progressBackHeightDPI = 160;
        final float radius = 11.1f * density;
        final String satoshi = balance.valueSatoshi + " " + getContext().getString(R.string.satoshi);
        final String btc = String.format("%.8f", balance.valueBTC) + " " + getContext().getString(R.string.btc);
        final String percentage = balance.percent + getContext().getString(R.string.percent);
        satoshiBalance.setText(satoshi);
        if (balance.valueSatoshi.compareTo(new BigDecimal(100)) >= 0) {
            satoshiBalance.setTextSize(30f);
        }
        if (balance.valueSatoshi.compareTo(new BigDecimal(1000)) >= 0) {
            satoshiBalance.setTextSize(26f);
        }
        btcBalance.setText(btc);
        percentageBalance.setText(percentage);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setSize((int) (progressBackWidthDPI * density), (int) (((balance.percent % 100) * 0.01f) * progressBackHeightDPI * density));
        shape.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        shape.setColors(new int[]{0xFFA9E141, 0xFF44CB46});
        progress_front.setBackground(shape);
    }

    private void setBoostButtonOnClick() {
        final ImageView rocket = getView().findViewById(R.id.rocket);
        final ImageButton boostButton = getView().findViewById(R.id.boost_button);
        final ValueAnimator rocketAnimator = ValueAnimator.ofFloat(0, 2);
        final float delta = 100;
        final int twoSecondsAndHalf = 2500;
        rocketAnimator.setDuration(twoSecondsAndHalf);
        rocketAnimator.addUpdateListener(animation -> {
            final float degreeRad = (float) ((float) animation.getAnimatedValue() * Math.PI);
            final float rocketTranslationX = (float) (delta * (Math.sin(degreeRad) + Math.sin(degreeRad / 2)));
            final float rocketTranslationY = (float) (delta * (Math.cos(degreeRad) - 1));
            final float rotation = (float) (180 * (Math.sin(degreeRad) * Math.sin(degreeRad * 2) / Math.PI));
            rocket.setTranslationX(rocketTranslationX);
            rocket.setTranslationY(rocketTranslationY);
            rocket.setRotation(rotation);
            rocket.animate();
        });
        final View[] servers = {
                getView().findViewById(R.id.first_server),
                getView().findViewById(R.id.second_server),
                getView().findViewById(R.id.third_server),
                getView().findViewById(R.id.forth_server)
        };
        final ImageView[] arrows = {
                getView().findViewById(R.id.first_arrow),
                getView().findViewById(R.id.second_arrow),
                getView().findViewById(R.id.third_arrow),
                getView().findViewById(R.id.forth_arrow)
        };
        final Integer[] serverPings = {6, 12, 25, 3};
        final AppTestApplication appTestApplication = (AppTestApplication) getActivity().getApplication();
        final ScheduledExecutorService executorService = appTestApplication.getExecutorService();
        boostButton.setOnClickListener(it -> {
            rocketAnimator.start();
            for (View server : servers) {
                server.setPressed(false);
            }
            for (int serverIndex = 0; serverIndex < servers.length; serverIndex++) {
                final int finalServerIndex = serverIndex;
                executorService.schedule(() -> {
                    servers[finalServerIndex].setPressed(true);
                    final RotateAnimation rotateAnimation = new RotateAnimation(0,
                            180f / Collections.max(Arrays.asList(serverPings)) * serverPings[finalServerIndex],
                            Animation.RELATIVE_TO_SELF,
                            0.5f,
                            Animation.RELATIVE_TO_SELF,
                            0.5f);
                    rotateAnimation.setDuration(500);
                    rotateAnimation.setFillAfter(true);
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    arrows[finalServerIndex].startAnimation(rotateAnimation);
                    if (finalServerIndex != 0) {
                        servers[finalServerIndex - 1].setPressed(false);
                    }
                }, finalServerIndex * 1000L, TimeUnit.MILLISECONDS);
            }
        });
    }
}
