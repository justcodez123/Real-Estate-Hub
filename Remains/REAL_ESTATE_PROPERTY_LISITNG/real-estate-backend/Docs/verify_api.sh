#!/bin/bash
# Diagnostic script to verify Recommend API setup

echo "=========================================="
echo "Recommend API - Diagnostic Check"
echo "=========================================="
echo ""

echo "1. Checking PropertyController autowiring..."
grep -n "@Autowired" src/main/java/com/realestate/controller/PropertyController.java | grep -E "propertyRepository|dotNetRecommendationClient"
echo ""

echo "2. Checking DotNetRecommendationClient..."
grep -n "public class DotNetRecommendationClient" src/main/java/com/realestate/service/DotNetRecommendationClient.java
echo ""

echo "3. Checking RestTemplate Configuration..."
grep -n "public RestTemplate restTemplate" src/main/java/com/realestate/config/RestTemplateConfig.java
echo ""

echo "4. Checking PropertyService autowiring..."
grep -n "private DotNetRecommendationClient" src/main/java/com/realestate/service/PropertyService.java
echo ""

echo "5. Checking recommend endpoint..."
grep -n "GET /recommendations" src/main/java/com/realestate/controller/PropertyController.java
grep -n "@GetMapping(\"/recommendations\")" src/main/java/com/realestate/controller/PropertyController.java
echo ""

echo "6. Checking for duplicate methods..."
grep -n "public.*recommend" src/main/java/com/realestate/service/PropertyService.java
echo ""

echo "=========================================="
echo "Diagnostic Complete"
echo "=========================================="
echo ""
echo "If all checks passed:"
echo "✓ Clean the IDE cache (File > Invalidate Caches)"
echo "✓ Rebuild the project (Build > Rebuild Project)"
echo "✓ Run Maven: mvn clean install -DskipTests"
