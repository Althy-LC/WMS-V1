Redis分布式锁及看门狗机制设计

<img width="324" height="297" alt="image" src=“https://github.com/user-attachments/assets/3bc387b8-59ea-43a4-b0f0-5acc02d69b7d” />
<img width="351" height="235" alt="图片" src="https://github.com/user-attachments/assets/f548aaf2-a64b-4c22-883d-cd62d78316b8" />
<img width="327" height="310" alt="图片" src="https://github.com/user-attachments/assets/d9d0fec0-a7d6-420d-9beb-d0470152bc89" />
<img width="309" height="287" alt="图片" src="https://github.com/user-attachments/assets/ec0819be-cf2c-402f-b29e-51c63d3fc223" />
<img width="329" height="290" alt="图片" src="https://github.com/user-attachments/assets/8c4047a0-c67f-4ab4-953f-adea06464dc2" />


S型路径优化规则设计


<img width="338" height="300" alt="图片" src="https://github.com/user-attachments/assets/04d56bcb-2a61-4db9-a756-e4f4e12888d0" />


波次聚合设计


<img width="287" height="296" alt="图片" src="https://github.com/user-attachments/assets/a5310ef2-10ac-4fa5-a814-240e345226cb" />
<img width="328" height="319" alt="图片" src="https://github.com/user-attachments/assets/a8d6cf89-74f7-468e-9e55-dda2b7dda15d" />
<img width="338" height="337" alt="图片" src="https://github.com/user-attachments/assets/9ab2e86b-fa0a-4a0b-b94a-55c277857d3a" />
<img width="365" height="330" alt="图片" src="https://github.com/user-attachments/assets/c7d82516-145d-4cae-99b4-cd98164386b1" />
<img width="292" height="286" alt="图片" src="https://github.com/user-attachments/assets/c332521f-ec06-436e-ab58-a05e0edb5dd2" />
<img width="307" height="290" alt="图片" src="https://github.com/user-attachments/assets/5c4314d9-7f25-48a8-9db3-c95c2e22ecb6" />

三层防超卖机制
  数据层面通过拆分库存为总库存可用库存销售锁定库存
  
  <img width="415" height="11" alt="image" src="https://github.com/user-attachments/assets/6d1e7f8d-5c58-45af-8c2a-4825e17a717c" />
  
并发层面

Redis分布式锁+MySQL乐观锁

  <img width="344" height="323" alt="image" src="https://github.com/user-attachments/assets/d9805b60-4270-4cc1-abe3-c6cbb76df3c0" />
