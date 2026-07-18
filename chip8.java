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

    void decode() {
        int nn, nnn, x;
        switch ((opcode & 0xF000) >> 12) {
            case 0:
                switch ((opcode & 0x00FF)) {
                    case 0xE0: // clear screen
                        for (int displayX = 0; displayX < display.length; displayX++) {
                            for (int displayY = 0; displayY < display[displayX].length; displayY++) {
                                display[displayX][displayY] = false;
                                // @TODO: 리랜더링 개발
                            }
                        }
                        break;
                    case 0xEE: // return
                        sp--;
                        pc = stack[sp];
                        break;
                    default:
                        break;
                }
                break;
            case 1: // jump
                nnn = opcode & 0x0FFF;
                pc = nnn;
                break;
            case 2: // call
                stack[sp] = pc;
                sp++;
                nnn = opcode & 0x0FFF;
                pc = nnn;
                break;
            case 3: // skip conditionally
                nn = opcode & 0x00FF;
                x = (opcode & 0x0F00) >> 8;
                if (V[x] == nn) {
                    pc += 2;
                }
                break;
        }
    }
}

