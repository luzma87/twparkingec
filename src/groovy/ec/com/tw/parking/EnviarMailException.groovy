package ec.com.tw.parking
/**
 * Created by lmunda on 1/6/16 16:36.
 */
class EnviarMailException extends Exception {

    public EnviarMailException(){
        super()
    }

    public EnviarMailException(String mensaje){
        super(mensaje)
    }
}
