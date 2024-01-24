package co.geisyanne.meuapp.presentation.common.base

interface RegisterCallback {

    fun onSuccess()
    fun onFailure(message: String)
    fun onComplete()

}