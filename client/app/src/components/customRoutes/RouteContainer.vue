<template>
    <div class="ticket-card">
      <div class="header">
        <div class="price">
          <span class="label">Самый дешевый</span>
          <div class="amount">
            {{ formatPrice(ticket.PRICE) }} ₽
            <span v-if="ticket.luggagePrice" class="luggage">
              + {{ formatPrice(ticket.luggagePrice) }} ₽
            </span>
          </div>
        </div>
        <button class="select-btn">Выбрать билет</button>
      </div>
  
      <div class="divider"></div>
  
      <div class="main-info">
        <div class="transport">
          <span class="prefix">Пойми:</span>
          {{ ticket.TRANSPORT }}
        </div>
  
        <div class="time-info">
          <div class="departure">
            <div class="time">{{ ticket.TIME }}</div>
            <div class="city">{{ ticket.ROUTE_FROM }}</div>
            <div class="date">{{ formatDate(ticket.DATE) }}</div>
          </div>
  
          <div class="duration">
            <div class="plane-icon">✈</div>
            В пути: {{ calculateDuration(ticket.TIME, ticket.ARRIVAL_TIME) }}
          </div>
  
          <div class="arrival">
            <div class="time">{{ ticket.ARRIVAL_TIME }}</div>
            <div class="city">{{ ticket.ROUTE_TO }}</div>
            <div class="date">{{ formatDate(ticket.DATE) }}</div>
          </div>
        </div>
  
        <div class="airport-codes">
          <span>{{ ticket.FROM_CODE }}</span>
          <span>→</span>
          <span>{{ ticket.TO_CODE }}</span>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>  

  
  const formatPrice = (price) => {
    return new Intl.NumberFormat('ru-RU').format(price);
  };
  
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const options = { day: 'numeric', month: 'short', year: 'numeric', weekday: 'short' };
    return date.toLocaleDateString('ru-RU', options);
  };
  
  const calculateDuration = (start, end) => {
    const [startHours, startMinutes] = start.split(':').map(Number);
    const [endHours, endMinutes] = end.split(':').map(Number);
    
    let diffHours = endHours - startHours;
    let diffMinutes = endMinutes - startMinutes;
    
    if (diffMinutes < 0) {
      diffHours--;
      diffMinutes += 60;
    }
    
    return `${diffHours}ч ${diffMinutes}м`;
  };
  </script>
  
  <style scoped>
  .ticket-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin: 16px;
    max-width: 600px;
  }
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }
  
  .price .label {
    font-size: 14px;
    color: #666;
  }
  
  .amount {
    font-size: 24px;
    font-weight: bold;
    color: #2d3748;
  }
  
  .luggage {
    font-size: 14px;
    color: #4a5568;
    margin-left: 8px;
  }
  
  .select-btn {
    background: #3182ce;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 6px;
    cursor: pointer;
    transition: background 0.3s;
  }
  
  .select-btn:hover {
    background: #2b6cb0;
  }
  
  .divider {
    border-bottom: 1px solid #e2e8f0;
    margin: 16px 0;
  }
  
  .time-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 20px 0;
  }
  
  .departure, .arrival {
    text-align: center;
  }
  
  .time {
    font-size: 24px;
    font-weight: 500;
    margin-bottom: 4px;
  }
  
  .city {
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 4px;
  }
  
  .date {
    font-size: 14px;
    color: #718096;
  }
  
  .duration {
    text-align: center;
    color: #718096;
  }
  
  .plane-icon {
    font-size: 24px;
    margin-bottom: 4px;
  }
  
  .airport-codes {
    display: flex;
    justify-content: center;
    gap: 8px;
    color: #4a5568;
    font-weight: 500;
  }
  
  .transport {
    color: #4a5568;
    margin-bottom: 12px;
  }
  
  .prefix {
    color: #718096;
  }
</style>