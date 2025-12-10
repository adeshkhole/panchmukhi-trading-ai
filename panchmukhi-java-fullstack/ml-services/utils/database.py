import os
import logging
from typing import Optional, Dict, Any
from motor.motor_asyncio import AsyncIOMotorClient
from redis import asyncio as aioredis
from pymongo import MongoClient

logger = logging.getLogger(__name__)

class MongoDBConnection:
    """MongoDB connection manager."""
    
    client: Optional[AsyncIOMotorClient] = None
    db = None
    
    @classmethod
    async def initialize(cls):
        """Initialize MongoDB connection."""
        try:
            mongo_uri = os.getenv("MONGODB_URI", "mongodb://panchmukhi_admin:panchmukhi_admin_pass@localhost:27017/?authSource=admin")
            db_name = os.getenv("MONGODB_DB", "panchmukhi_logs")
            
            cls.client = AsyncIOMotorClient(mongo_uri)
            cls.db = cls.client[db_name]
            
            # Test the connection
            await cls.client.admin.command('ping')
            logger.info("MongoDB connection established successfully")
            
        except Exception as e:
            logger.error(f"Failed to connect to MongoDB: {e}")
            raise
    
    @classmethod
    def get_database(cls):
        """Get the database instance."""
        if cls.db is None:
            raise RuntimeError("MongoDB not initialized. Call initialize() first.")
        return cls.db
    
    @classmethod
    async def close(cls):
        """Close the MongoDB connection."""
        if cls.client:
            cls.client.close()
            logger.info("MongoDB connection closed")
            
    @classmethod
    async def test_connection(cls):
        """Test the MongoDB connection."""
        try:
            if cls.client is None:
                await cls.initialize()
            await cls.client.admin.command('ping')
            return True
        except Exception as e:
            logger.error(f"MongoDB connection test failed: {e}")
            return False


class RedisConnection:
    """Redis connection manager."""
    
    redis: Optional[aioredis.Redis] = None
    
    @classmethod
    async def initialize(cls):
        """Initialize Redis connection."""
        try:
            redis_host = os.getenv("REDIS_HOST", "localhost")
            redis_port = int(os.getenv("REDIS_PORT", 6379))
            redis_db = int(os.getenv("REDIS_DB", 0))
            
            cls.redis = aioredis.Redis(
                host=redis_host,
                port=redis_port,
                db=redis_db,
                decode_responses=True
            )
            
            # Test the connection
            await cls.redis.ping()
            logger.info("Redis connection established successfully")
            
        except Exception as e:
            logger.error(f"Failed to connect to Redis: {e}")
            raise
    
    @classmethod
    def get_redis(cls) -> aioredis.Redis:
        """Get the Redis client instance."""
        if cls.redis is None:
            raise RuntimeError("Redis not initialized. Call initialize() first.")
        return cls.redis
    
    @classmethod
    async def close(cls):
        """Close the Redis connection."""
        if cls.redis:
            await cls.redis.close()
            logger.info("Redis connection closed")
