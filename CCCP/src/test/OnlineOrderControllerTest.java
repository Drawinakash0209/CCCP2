package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.service.OnlineOrderServiceInterface;


class OnlineOrderControllerTest {
	
	@Mock
	private OnlineOrderServiceInterface onlineOrderService;
	
	@InjectMocks
	private OnlineOrderControllerTest controller;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
