import http from 'k6/http';
import { check, sleep } from 'k6';
const VUE_APP_BACKEND_API_FOR_TESTS="http://172.17.0.1:8080/api"

export const options = {
  thresholds:{
    http_req_duration: ['p(95)<2000']
  },
  stages: [
    { duration: '5s', target: 30, startVUs: 30 }, 
    { duration: '1m', target: 0 },   
    { duration: '15s', target: 10 }, 
    { duration: '10s', target: 0}
  ],
  vus: 10, 
};

export default function () {
  const res = http.get(`${VUE_APP_BACKEND_API_FOR_TESTS}/routes`);

  check(res, {
    'status is 200': (r) => r.status === 200,
    'rate limit triggered: ': (r) => r.status === 429
  }); 

  sleep(1);
}
