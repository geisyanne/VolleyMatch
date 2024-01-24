package co.geisyanne.meuapp.presentation.common.base

interface BasePresenter {

    fun onDestroy() // TAKE REF FROM THE VIEW FROM INSIDE THE PRESENT WHEN IT IS DESTROYED

}