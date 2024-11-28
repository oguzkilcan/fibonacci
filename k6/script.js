import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 10,
  duration: '30s',
}

export default function() {
  http.get('http://localhost:8080/fibonacci/999999?pageCount=200000&pageSize=5');
  sleep(1);
}
