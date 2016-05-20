package com.moybl.yaynay.backend;

import android.support.annotation.NonNull;

public interface YayNayResultCallback<T extends YayNayResult> {

	void onResult(@NonNull T result);

}
