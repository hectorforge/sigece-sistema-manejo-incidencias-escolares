package pe.gob.minedu.sigece.usecases.auth;

import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderPort implements IPasswordEncoderPort{
    @Override
    public String encode(CharSequence rawPassword) {
        return "";
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
