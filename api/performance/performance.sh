K6_WEB_DASHBOARD=true k6 run -e \
  FRONT_URL=front_url\
  BACK_URL=back_url\
  k6-2k-load.ts