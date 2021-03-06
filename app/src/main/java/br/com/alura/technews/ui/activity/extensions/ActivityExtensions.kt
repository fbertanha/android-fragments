package br.com.alura.technews.ui.activity.extensions

import android.app.Activity
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun Activity.mostraErro(mensagem: String) {
    Toast.makeText(
        this,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}

fun AppCompatActivity.addFragment(@IdRes containerId: Int, fragment: Fragment) {
    this.supportFragmentManager
        .beginTransaction()
        .add(containerId, fragment, fragment::class.java.name)
        .commit()
}

fun AppCompatActivity.replaceFragment(@IdRes containerId: Int, fragment: Fragment) {
    this.supportFragmentManager
        .beginTransaction()
        .replace(containerId, fragment)
        .addToBackStack(fragment::class.java.name)
        .commit()
}

fun AppCompatActivity.transactionFragment(executeTransaction : FragmentTransaction.() -> Unit){
    val beginTransaction = supportFragmentManager
        .beginTransaction()

    executeTransaction(beginTransaction)
    beginTransaction.commit()
}