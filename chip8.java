public class chip8 {
    // 필드: 메모리, V 레지스터, PC, I 레지스터, opcode, 스택, 스택포인터, 타이머 2개, 디스플레이, 키보드
    byte[] memory = new byte[4096];
    int[] V = new int[16];
    int I = 0;
    int opcode;
    int pc = 0x200;
    int[] stack = new int[16];
    int sp = 0;
    int soundTimer;
    int delayTimer;
    boolean[][] display = new boolean[64][32]; // [x][y]
    boolean[] pressed = new boolean[16];
    
    void fetch() {
        opcode = memory[pc << 8] | memory[pc + 1];
        pc += 2;
    }
}

