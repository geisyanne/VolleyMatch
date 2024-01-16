package co.geisyanne.meuapp.common.base

interface RegisterCallback {

    fun onSuccess()
    fun onFailure(message: String)
    fun onComplete()

}