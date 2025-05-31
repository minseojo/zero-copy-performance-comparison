# Zero-Copy vs Traditional I/O 성능 비교

이 프로젝트는 Java를 사용하여 **전통적인 파일 전송 방식**과 **Zero-Copy 방식 (FileChannel.transferTo)** 간의 **성능 차이**를 비교합니다.

##  목적

- 전송 방식에 따른 CPU 부하, 전송 속도, 캐싱 효과 등의 차이를 정량적으로 확인
- Java NIO의 `transferTo()`가 내부적으로 사용하는 `sendfile()` 시스템 콜의 효과 분석
- 파일 전송에서 Zero-Copy의 캐시 활용 성능 향상 확인

---

## 파일 만드는 법
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