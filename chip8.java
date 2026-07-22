import java.util.Random;

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
        int nn, nnn, x, y;
        switch ((opcode & 0xF000) >> 12) {
            case 0x0:
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
            case 0x1: // jump
                nnn = opcode & 0x0FFF;
                pc = nnn;
                break;
            case 0x2: // call
                stack[sp] = pc;
                sp++;
                nnn = opcode & 0x0FFF;
                pc = nnn;
                break;
            case 0x3: // skip conditionally
                nn = opcode & 0x00FF;
                x = (opcode & 0x0F00) >> 8;
                if (V[x] == nn) {
                    pc += 2;
                }
                break;
            case 0x4: // skip conditionally
                nn = opcode & 0x00FF;
                x = (opcode & 0x0F00) >> 8;
                if (V[x] == nn) {
                    pc += 2;
                }
                break;
            case 0x5: // skip conditionally 
                x = (opcode & 0x0F00) >> 8;
                y = (opcode & 0x00F0) >> 4;
                if (V[x] == V[y]) {
                    pc += 2;
                }
                break;
            case 0x9: // skip conditionally 
                x = (opcode & 0x0F00) >> 8;
                y = (opcode & 0x00F0) >> 4;
                if (V[x] != V[y]) {
                    pc += 2;
                }
                break;
            case 0x6: // set
                x = (opcode & 0x0F00) >> 8;
                nn = opcode & 0x00FF;
                V[x] = nn;
                break;
            case 0x7: // add
                x = (opcode & 0x0F00) >> 8;
                nn = opcode & 0x00FF;
                V[x] += nn;
                break;
            case 0x8: 
                int n = opcode & 0x000F;
                switch (n) {
                    case 0x0: // set
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] = V[y];
                        break;
                    case 0x1: // set V[x] binary or V[x], V[y]
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] = V[x] | V[y];
                        break;
                    case 0x2: // set V[x] binary and V[x], V[y]
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] = V[x] & V[y];
                        break;
                    case 0x3: // set V[x] binary xor V[x], V[y] 
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] = V[x] ^ V[y];
                        break;
                    case 0x4: // add 
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] += V[y];
                        break;
                    case 0x5: // subtract
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] = V[x] - V[y];
                        break;
                    case 0x6: // shift left
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] = V[y];
                        V[x] = V[x] >> 1;
                        break;
                    case 0x7: // add
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] = V[y] - V[x];
                        break;
                    case 0xE: // shif right
                        x = (opcode & 0x0F00) >> 8;
                        y = (opcode & 0x00F0) >> 4;
                        V[x] = V[y];
                        V[x] = V[x] << 1;
                        break;
                    default:
                        break;
                }
            case 0xA: // set index
                I = opcode & 0x0FFF;
                break;
            case 0xB: // jump with offset
                nnn = opcode & 0x0FFF;
                pc = nnn + V[0];
                break;
            case 0xC: // random
                Random random = new Random();
                int r = random.nextInt(256);
                nn = opcode & 0x00FF;
                x = (opcode & 0x0F00) >> 12;
                V[x] = r & nn;
                break;
        }
    }
}

