SELECT * FROM(SELECT T1.JOB_NO AS JOB_NO, T1.JOB_DESCRIPTION,  T1.PHOTO_FROM_DATE, T1.PHOTO_TO_DATE, T1.PHOTO_FROM_TIME,
  T1.PHOTO_TO_TIME, T1.PROGRAMME_NO, T1.FROM_EPISODE_NO, T1.TO_EPISODE_NO, T1.REQUEST_DEPARTMENT, T1.REMARKS, T1.MODIFIED_BY, T1.MODIFIED_AT,
  ' ' AS COL_TYPE, 0 AS COL_UNIT_COST, 0 AS COL_QTY, 0 AS COL_AMOUNT, T3.PROGRAMME_NAME
  FROM ARC_PHOTO_CONSUMPTION_HEADER T1, ARC_PROGRAMME_MASTER T3
 WHERE T1.MODIFIED_AT BETWEEN '%s' AND '%s'
   AND T1.PROGRAMME_NO = T3.PROGRAMME_NO
   AND T1.JOB_NO NOT IN(SELECT T5.JOB_NO FROM ARC_PHOTO_CONSUMPTION_ITEM T5)
   AND T1.JOB_NO NOT IN(SELECT T6.JOB_NO FROM ARC_PHOTO_OTHER_MATERIAL_CONSUMPTION T6)
   AND T1.JOB_NO NOT IN(SELECT T7.JOB_NO FROM ARC_PHOTO_LABOUR_CONSUMPTION T7)
UNION
SELECT T1.JOB_NO AS JOB_NO, T1.JOB_DESCRIPTION,  T1.PHOTO_FROM_DATE, T1.PHOTO_TO_DATE, T1.PHOTO_FROM_TIME,
  T1.PHOTO_TO_TIME, T1.PROGRAMME_NO, T1.FROM_EPISODE_NO, T1.TO_EPISODE_NO, T1.REQUEST_DEPARTMENT, T1.REMARKS, T1.MODIFIED_BY, T2.MODIFIED_AT,
  T2.ACCOUNT_ALLOCATION || '-' || T4.ACTUAL_ACCOUNT_DESCRIPTION AS COL_TYPE, T2.UNIT_COST AS COL_UNIT_COST, T2.CONSUMPTION_QUANTITY AS COL_QTY, (T2.CONSUMPTION_QUANTITY * T2.UNIT_COST) AS COL_AMOUNT,
  T3.PROGRAMME_NAME
  FROM ARC_PHOTO_CONSUMPTION_HEADER T1, ARC_PHOTO_CONSUMPTION_ITEM T2, ARC_PROGRAMME_MASTER T3, ARC_ACCOUNT_ALLOCATION T4
 WHERE T1.JOB_NO = T2.JOB_NO
   AND T1.PROGRAMME_NO = T3.PROGRAMME_NO
   AND T2.ACCOUNT_ALLOCATION = T4.ACTUAL_ACCOUNT_ALLOCATION
   AND T2.MODIFIED_AT BETWEEN '%s' AND '%s'
UNION
 SELECT T1.JOB_NO AS JOB_NO, T1.JOB_DESCRIPTION,  T1.PHOTO_FROM_DATE, T1.PHOTO_TO_DATE, T1.PHOTO_FROM_TIME,
  T1.PHOTO_TO_TIME, T1.PROGRAMME_NO, T1.FROM_EPISODE_NO, T1.TO_EPISODE_NO, T1.REQUEST_DEPARTMENT, T1.REMARKS, T1.MODIFIED_BY, T2.MODIFIED_AT, 
  T2.ACCOUNT_ALLOCATION || '-' || T4.ACTUAL_ACCOUNT_DESCRIPTION AS COL_TYPE, T2.UNIT_COST AS COL_UNIT_COST, 0 AS COL_QTY, T2.OTHER_MATERIAL_AMOUNT AS COL_AMOUNT,
  T3.PROGRAMME_NAME
  FROM ARC_PHOTO_CONSUMPTION_HEADER T1, ARC_PHOTO_OTHER_MATERIAL_CONSUMPTION T2, ARC_PROGRAMME_MASTER T3, ARC_ACCOUNT_ALLOCATION T4
 WHERE T1.JOB_NO = T2.JOB_NO
   AND T1.PROGRAMME_NO = T3.PROGRAMME_NO
   AND T2.ACCOUNT_ALLOCATION = T4.ACTUAL_ACCOUNT_ALLOCATION
   AND T2.MODIFIED_AT BETWEEN '%s' AND '%s'
UNION
 SELECT T1.JOB_NO AS JOB_NO, T1.JOB_DESCRIPTION,  T1.PHOTO_FROM_DATE, T1.PHOTO_TO_DATE, T1.PHOTO_FROM_TIME,
  T1.PHOTO_TO_TIME, T1.PROGRAMME_NO, T1.FROM_EPISODE_NO, T1.TO_EPISODE_NO, T1.REQUEST_DEPARTMENT, T1.REMARKS, T1.MODIFIED_BY, T2.MODIFIED_AT,
  T2.LABOUR_TYPE || '-' || T4.LABOUR_TYPE_DESCRIPTION AS COL_TYPE, T2.HOURLY_RATE AS COL_UNIT_COST, T2.NO_OF_HOURS AS COL_QTY, (T2.NO_OF_HOURS * T2.HOURLY_RATE) AS COL_AMOUNT,
  T3.PROGRAMME_NAME
  FROM ARC_PHOTO_CONSUMPTION_HEADER T1, ARC_PHOTO_LABOUR_CONSUMPTION T2, ARC_PROGRAMME_MASTER T3, ARC_LABOUR_TYPE T4
 WHERE T1.JOB_NO = T2.JOB_NO
   AND T1.PROGRAMME_NO = T3.PROGRAMME_NO
   AND T1.PROGRAMME_NO = T3.PROGRAMME_NO
   AND T2.LABOUR_TYPE = T4.LABOUR_TYPE
   AND T2.MODIFIED_AT BETWEEN '%s' AND '%s'
   AND T4.EFFECTIVE_FROM_DATE <= T1.INPUT_DATE
   AND T4.EFFECTIVE_TO_DATE   >= T1.INPUT_DATE )
 ORDER BY MODIFIED_AT, JOB_NO