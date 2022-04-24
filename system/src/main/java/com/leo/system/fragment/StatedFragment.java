package com.leo.system.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by NyatoLEO on 2017/2/28.
 */

public abstract class StatedFragment extends Fragment {
    /**
     * 是否已经初始化过
     */
    public boolean isInitialized;
    protected Context context;
    protected Bundle savedState;

    public StatedFragment() {
        super();
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstLaunched();
        }
        bindViewData();
    }

    /**
     * 首次加載，可以从argument加载一些数据
     */
    protected abstract void onFirstLaunched();

    protected abstract void bindViewData();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Save State Here
        saveStateToArguments();
    }

    /**
     * Don't Touch !!
     */
    private void saveStateToArguments() {
        if (getView() != null) {
            savedState = saveState();
        }
        if (savedState != null) {
            Bundle b = getArguments();
            if (b != null) {
                b.putBundle("internalSavedViewState8954201239547", savedState);
            }
        }
    }

    /**
     * Don't Touch !!
     *
     * @return
     */
    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b != null) {
            savedState = b.getBundle("internalSavedViewState8954201239547");
        }
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    /**
     * Restore Instance State Here
     */
    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString("text"));
            onRestoreState(savedState);
        }
    }

    protected abstract void onRestoreState(Bundle savedInstanceState);

    /**
     * Save Instance State Here
     *
     * @return
     */
    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    protected abstract void onSaveState(Bundle outState);
}