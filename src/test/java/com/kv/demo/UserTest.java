package com.kv.demo;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

public class UserTest {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 30; i++) {
            AnaUser anaUser = new AnaUser(latch);
            anaUser.start();
        }
        latch.countDown();
    }

    static class AnaUser extends Thread {
        CountDownLatch latch;

        public AnaUser(CountDownLatch latch) {
            super();
            this.latch = latch;
        }
        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            requestNow();
//            requestVersion();
        }

        public void requestNow() {
            RestTemplate restTemplate = new RestTemplate();
//            Integer result = restTemplate.getForObject("http://localhost:8080/update/user/1",Integer.class);
//            Integer result = restTemplate.getForObject("http://localhost:8080/update/userbyversion/3",Integer.class);
            Integer result = restTemplate.getForObject("http://localhost:8080/update/useAop/5",Integer.class);
            System.out.println("now is:"+result);
        }

    }
}
