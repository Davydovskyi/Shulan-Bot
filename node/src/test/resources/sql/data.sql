INSERT INTO raw_data (id, event)
VALUES (1, '{
  "message": {
    "chat": {
      "id": 925392782,
      "type": "private",
      "username": "CatTomcat93",
      "last_name": "Davydovskyi",
      "first_name": "Anton"
    },
    "date": 1709565695,
    "from": {
      "id": 925392782,
      "is_bot": false,
      "username": "CatTomcat93",
      "last_name": "Davydovskyi",
      "first_name": "Anton",
      "language_code": "ru"
    },
    "text": "hello",
    "message_id": 52
  },
  "update_id": 739915780
}'),
       (2, '{
         "message": {
           "chat": {
             "id": 925392782,
             "type": "private",
             "username": "CatTomcat93",
             "last_name": "Davydovskyi",
             "first_name": "Anton"
           },
           "date": 1709565826,
           "from": {
             "id": 925392782,
             "is_bot": false,
             "username": "CatTomcat93",
             "last_name": "Davydovskyi",
             "first_name": "Anton",
             "language_code": "ru"
           },
           "text": "hello",
           "message_id": 54
         },
         "update_id": 739915781
       }');
SELECT setval('raw_data_id_seq', (SELECT MAX(id) FROM raw_data), true);