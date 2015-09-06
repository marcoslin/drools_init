package it.twopay.entity;

public enum CarGears {
	GEAR_1(1), GEAR_2(2), GEAR_3(3),
	GEAR_4(4), GEAR_5(5), GEAR_6(6);
	private Integer gear;
	private CarGears(Integer gear) {
		this.gear = gear;
	}
	public int getValue() {
		return gear;
	}
	public CarGears getByValue(int gear) {
		CarGears result = null;
		switch (gear) {
		case 1:
			result = GEAR_1;
			break;
		case 2:
			result = GEAR_2;
			break;
		case 3:
			result = GEAR_3;
			break;
		case 4:
			result = GEAR_4;
			break;
		case 5:
			result = GEAR_5;
			break;
		case 6:
			result = GEAR_6;
			break;
		}
		return result;
	}
	@Override 
    public String toString(){ 
        return "GEAR_" + gear; 
    }
}
