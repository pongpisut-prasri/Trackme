package manservice.trackmeh.foodtracking.security.jwt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());
		// ตรวจสอบว่า response ไม่ได้ถูก commit และไม่มี error ที่ถูกส่งไปแล้ว
		if (!response.isCommitted() && response.getStatus() == HttpServletResponse.SC_OK) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
		}
	}

}
