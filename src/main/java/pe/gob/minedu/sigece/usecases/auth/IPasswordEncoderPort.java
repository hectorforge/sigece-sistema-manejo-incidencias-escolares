package pe.gob.minedu.sigece.usecases.auth;

public interface IPasswordEncoderPort {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}