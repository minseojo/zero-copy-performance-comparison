### [**Zero-Copy 정리 블로그 링크**](https://virtualworld.tistory.com/entry/IO-%EB%B3%91%EB%AA%A9%EC%9D%84-%EC%97%86%EC%95%A0%EB%8A%94-%EB%B0%A9%EB%B2%95-Zero-Copy-feat-sendfile-DMA)

</br>

# Zero-Copy vs Traditional I/O 성능 비교
**기존의 파일 전송 방식(전통적인 방식, read(), write())** 과 **Zero-Copy 방식 (FileChannel.transferTo)** 간의 **성능 차이**를 비교합니다.

</br>

##  목적

- 전송 방식에 따른 CPU 부하, 전송 속도, 캐싱 효과 등의 차이를 정량적으로 확인
- Java NIO의 `transferTo()`가 내부적으로 사용하는 `sendfile()` 시스템 콜의 효과 분석
- 파일 전송에서 Zero-Copy의 캐시 활용 성능 향상 확인

---


## 예제 파일 만드는 법
다음 코드를 이용해서 .bin파일을 만들어서 사용하세요.

```python
sizes_in_mb = [1, 10, 50, 100, 250, 500, 1000]

def create_file(size_mb):
    filename = f"{size_mb}mb.bin"
    size_bytes = size_mb * 1024 * 1024  # MB to Bytes
    with open(filename, "wb") as f:
        f.write(b'\0' * size_bytes)
    print(f"{filename} created ({size_bytes:,} bytes)")

for size in sizes_in_mb:
    create_file(size)
```


## 결과
| **파일 크기** | **기존 방식** | **전송 시간(ms)** | **Zero-Copy 최초 전송시간 (ms)** | **Zero-Copy 캐시 이후 전송 시간 (ms)** |
| --- | --- | --- | --- | --- |
| 10MB | 13 | 13 | 8 | 차이 미미 / 캐싱 효과 적음 |
| 50MB | 32 | 25 | 13 | 약 48% 향상 / 캐싱 영향 존재 |
| 100MB | 60 | 46 | 22 | 약 52% 향상 / 캐싱 효과 뚜렷 |
| 250MB | 137 | 100 | 42 | 약 58% 향상 / 캐싱 효과 큼 |
| 500MB | 335 | 166 | 72 | 전송 시간 절반 이하 / 효과 큼 |
| 1GB | 770 | 526 | 113 | 약 78% 향상 / 대용량에서 효과 극대화 |
