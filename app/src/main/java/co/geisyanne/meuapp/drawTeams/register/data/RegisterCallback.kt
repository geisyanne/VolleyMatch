package co.geisyanne.meuapp.drawTeams.register.data

interface RegisterCallback {

    fun onSuccess()
    fun onFailure(message: String)
    fun onComplete()

}