package com.ascri.failtok.utilities.extensions

import android.os.Handler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable = apply { compositeDisposable.add(this) }

fun Handler.runDelayed(delayInMillis: Long, runLambda: ()->Unit) = this.postDelayed(Runnable(runLambda),delayInMillis)