package com.littlewind.demo.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.littlewind.demo.service.UserDetailsServiceImpl;
import com.littlewind.demo.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServiceImpl jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");
//		System.out.println(	"\nJwtRequestFilter_requestTokenHeader: "+requestTokenHeader + "\n");
		logger.debug("requestTokenHeader: "+requestTokenHeader + "\n");

		String email = null;
		String jwtToken = null;
		// if JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null) {
			if (requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
			} else {			
				jwtToken = requestTokenHeader;
			}

			try {
				email = jwtTokenUtil.getUsernameFromToken(jwtToken);
//				System.out.println("JwtRequestFilter_Email from token: "+email);
				logger.debug("Email from token: "+email);
			} catch (IllegalArgumentException e) {
//				System.out.println("Unable to get JWT Token");
				logger.error("Unable to get JWT Token");
				e.printStackTrace();
			} catch (ExpiredJwtException e) {
//				System.out.println("JWT Token has expired");
				logger.error("\nJWT Token has expired\n");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		// Once we get the token validate it.
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(email);

			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

}
