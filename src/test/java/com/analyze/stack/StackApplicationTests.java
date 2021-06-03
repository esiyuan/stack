package com.analyze.stack;

import com.analyze.stack.process.CalStackProcess;
import com.analyze.stack.process.StackGetProcess;
import com.analyze.stack.service.StackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StackApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private CalStackProcess calStackProcess;

    @Autowired
    private StackService stackService;
    @Autowired
    private StackGetProcess stackGetProcess;

    @Test
    public void inject() {
//        stackService.list();
        calStackProcess.calcStack();
    }

    @Test
    public void tet() {
        stackGetProcess.getddTotal();
    }
    @Test
    public void test01() {
        calStackProcess.calcDay("2021-06-03");
    }

}
