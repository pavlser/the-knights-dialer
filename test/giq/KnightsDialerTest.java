package giq;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class KnightsDialerTest {
	
	KnightsDialer kd = new KnightsDialer();

	@org.junit.jupiter.api.Test
	void test_is_valid_position() {
		assertTrue(kd.isValidPosition(new Pos(0, 0)));
		assertFalse(kd.isValidPosition(new Pos(-1, 0)));
		assertFalse(kd.isValidPosition(new Pos(0, -1)));
		assertFalse(kd.isValidPosition(new Pos(4, 4)));
	}
	
	@org.junit.jupiter.api.Test
	void test_cell_value() {
		assertEquals(1, kd.cellValue(new Pos(0, 0)));
		assertEquals(2, kd.cellValue(new Pos(1, 0)));
		assertEquals(3, kd.cellValue(new Pos(2, 0)));
		assertEquals(4, kd.cellValue(new Pos(0, 1)));
		assertEquals(5, kd.cellValue(new Pos(1, 1)));
		assertEquals(6, kd.cellValue(new Pos(2, 1)));
		assertEquals(7, kd.cellValue(new Pos(0, 2)));
		assertEquals(8, kd.cellValue(new Pos(1, 2)));
		assertEquals(9, kd.cellValue(new Pos(2, 2)));
		assertEquals(-1, kd.cellValue(new Pos(0, 3)));
		assertEquals(0, kd.cellValue(new Pos(1, 3)));
		assertEquals(-1, kd.cellValue(new Pos(2, 3)));
	}
	
	@org.junit.jupiter.api.Test
	void test_possible_hops() {
		List<Pos> hops = kd.getPossibleHops(new Pos(0, 0));
		assertEquals(2, hops.size());
		assertEquals(new Pos(2, 1), hops.get(0));
		assertEquals(new Pos(1, 2), hops.get(1));
	}

}
